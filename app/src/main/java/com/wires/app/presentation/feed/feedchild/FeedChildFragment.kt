package com.wires.app.presentation.feed.feedchild

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.domain.paging.PagingLoadStateAdapter
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.getColorAttribute
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
        setupPostsList()
        swipeRefreshLayoutFeedChild.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
        swipeRefreshLayoutFeedChild.setOnRefreshListener {
            callOperations()
            isRefreshingBySwipe = true
        }
        stateViewFlipperFeedChild.setRetryMethod { callOperations() }
        (recyclerViewFeedChildPosts.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerViewFeedChildPosts.emptyView = emptyViewFeedChildPosts
        parentFragment?.setFragmentResultListener(PostFragment.LIKE_CHANGED_RESULT_KEY) { _, bundle ->
            val postId = bundle.getInt(PostFragment.POST_ID_RESULT_KEY)
            if (postId != 0) postsAdapter.updatePostLike(postId)
        }
        parentFragment?.setFragmentResultListener(PostFragment.COMMENTS_CHANGED_RESULT_KEY) { _, bundle ->
            postsAdapter.updatePostComments(
                bundle.getInt(PostFragment.POST_ID_RESULT_KEY),
                bundle.getInt(PostFragment.COMMENTS_COUNT_RESULT_KEY)
            )
        }
        Unit
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
        openPostLiveEvent.observe { postId ->
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostGraph(postId))
        }
        openProfileLiveEvent.observe { userId ->
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToProfileGraph(userId))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (parentFragment as? OnFeedChildEventListener)?.let {
            onFeedChildEventListener = it
        }
    }

    private fun setupPostsList() = with(binding.recyclerViewFeedChildPosts) {
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
            addLoadStateListener(viewModel::bindLoadingState)
        }.withLoadStateFooter(PagingLoadStateAdapter { postsAdapter.retry() })
        addVerticalDividerItemDecoration()
    }
}
