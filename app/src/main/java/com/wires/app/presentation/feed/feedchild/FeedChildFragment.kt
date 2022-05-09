package com.wires.app.presentation.feed.feedchild

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.paging.PagingData
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.domain.paging.PagingLoadStateAdapter
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.createLoadableResultDialog
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showAlertDialog
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showToast
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.feed.FeedFragmentDirections
import com.wires.app.presentation.post.PostFragment
import timber.log.Timber
import javax.inject.Inject

class FeedChildFragment(private val interest: UserInterest?) : BaseFragment(R.layout.fragment_feed_child) {

    companion object {
        fun newInstance(interest: UserInterest?): FeedChildFragment {
            return FeedChildFragment(interest)
        }
    }

    private val viewModel: FeedChildViewModel by appViewModels()
    private val binding by viewBinding(FragmentFeedChildBinding::bind)

    @Inject lateinit var postsAdapter: PostsAdapter

    private var onFeedChildEventListener: OnFeedChildEventListener? = null

    /**
     * Маркер для того, чтобы отличать, обновляется ли экран с постами по свайпу PTR, или по запросу инициализации
     */
    private var isRefreshingBySwipe = false

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        swipeRefreshLayoutFeedChild.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
        swipeRefreshLayoutFeedChild.setOnRefreshListener {
            callOperations()
            isRefreshingBySwipe = true
        }
        stateViewFlipperFeedChild.setRetryMethod { callOperations() }
        setupResultListeners()
        setupPostsList()
    }

    override fun onBindViewModel() = with(viewModel) {
        userLiveData.observe { result ->
            if (!result.isSuccess) binding.stateViewFlipperFeedChild.setStateFromResult(result)
            result.doOnSuccess {
                if (postsAdapter.itemCount == 0 || isRefreshingBySwipe) getPosts(interest)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        postsLiveData.observe { data ->
            postsAdapter.submitData(lifecycle, data)
        }
        postsLoadingStateLiveData.observe { result ->
            if (isRefreshingBySwipe) {
                binding.swipeRefreshLayoutFeedChild.isRefreshing = result.isLoading
                result.doOnComplete { isRefreshingBySwipe = false }
                result.doOnFailure { showToast(it.message) }
                result.doOnSuccess {
                    binding.recyclerViewFeedChildPosts.scrollToPosition(0)
                    onFeedChildEventListener?.onLoadingStateChanged(result)
                }
            } else {
                binding.stateViewFlipperFeedChild.setStateFromResult(result)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        postLikeLiveEvent.observe { result ->
            result.doOnSuccess { likeResult ->
                likeResult.error?.let { error ->
                    postsAdapter.updatePostLike(likeResult.postId)
                    Timber.e(error.message)
                    showSnackbar(error.message)
                }
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
        val loadableResultDialog = createLoadableResultDialog()
        postDeleteLiveEvent.observe { result ->
            loadableResultDialog.setState(result)
            result.doOnSuccess { postId ->
                removePostFromList(postId)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
        openPostLiveEvent.observe { postId ->
            navigateTo(FeedFragmentDirections.actionFeedFragmentToPostGraph(postId))
        }
        openProfileLiveEvent.observe { userId ->
            navigateTo(FeedFragmentDirections.actionFeedFragmentToProfileGraph(userId))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFeedChildEventListener = parentFragment as? OnFeedChildEventListener
    }

    private fun setupPostsList() = with(binding.recyclerViewFeedChildPosts) {
        (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        emptyView = binding.emptyViewFeedChildPosts
        adapter = postsAdapter.apply {
            onPostClick = viewModel::openPost
            onAuthorClick = viewModel::openProfile
            onLikeClick = { postId, isLiked ->
                viewModel.setPostLike(postId, isLiked)
                postsAdapter.updatePostLike(postId)
            }
            onEditClick = { postId ->
                onFeedChildEventListener?.onOpenPostUpdate(postId)
            }
            onDeleteClick = { postId ->
                showAlertDialog(
                    titleRes = R.string.dialog_post_delete_title,
                    messageRes = R.string.dialog_post_delete_message,
                    positiveButtonTextRes = R.string.dialog_yes,
                    negativeButtonTextRes = R.string.dialog_no,
                    positiveButtonListener = { dialog, _ ->
                        viewModel.deletePost(postId)
                        dialog.dismiss()
                    }
                )
            }
            addLoadStateListener(viewModel::bindLoadingState)
        }.withLoadStateFooter(PagingLoadStateAdapter { postsAdapter.retry() })
        addVerticalDividerItemDecoration()
    }

    private fun setupResultListeners() = parentFragment?.run {
        setFragmentResultListener(PostFragment.LIKE_CHANGED_RESULT_KEY) { _, bundle ->
            val postId = bundle.getInt(PostFragment.POST_ID_RESULT_KEY)
            if (postId != 0) postsAdapter.updatePostLike(postId)
        }
        setFragmentResultListener(PostFragment.COMMENTS_CHANGED_RESULT_KEY) { _, bundle ->
            postsAdapter.updatePostComments(
                bundle.getInt(PostFragment.POST_ID_RESULT_KEY),
                bundle.getInt(PostFragment.COMMENTS_COUNT_RESULT_KEY)
            )
        }
        setFragmentResultListener(PostFragment.POST_UPDATED_RESULT_KEY) { _, bundle ->
            bundle.getParcelable<Post>(PostFragment.POST_DATA_RESULT_KEY)?.let { postsAdapter.updatePostData(it) }
        }
        setFragmentResultListener(PostFragment.POST_DELETED_RESULT_KEY) { _, bundle ->
            removePostFromList(bundle.getInt(PostFragment.POST_ID_RESULT_KEY))
        }
    }

    private fun removePostFromList(postId: Int) = with(postsAdapter) {
        removePost(postId)
        if (isEmpty) submitData(lifecycle, PagingData.empty())
    }
}
