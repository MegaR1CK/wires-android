package com.wires.app.presentation.feed.feedchild

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.domain.paging.PagingLoadStateAdapter
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.showToast
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.createpost.CreatePostFragment
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

    private var onLoadingStateChangedListener: OnLoadingStateChangedListener? = null
    private var isReturnedFromPost = false

    /**
     * Маркер для того, чтобы отличать, обновляется ли экран с постами по свайпу, или по запросу пагинации
     */
    private var isRefreshingBySwipe = false

    /**
     * Маркер первой загрузки списка постов
     */
    private var isFirstLoading = true

    override fun callOperations() {
        viewModel.getPosts(interest)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        setupPostsList()
        swipeRefreshLayoutFeedChild.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
        swipeRefreshLayoutFeedChild.setOnRefreshListener {
            callOperations()
            isRefreshingBySwipe = true
        }
        parentFragment?.setFragmentResultListener(CreatePostFragment.POST_CREATED_RESULT_KEY) { _, _ ->
            callOperations()
        }
        parentFragment?.setFragmentResultListener(PostFragment.POST_RETURN_KEY) { _, _ ->
            // TODO: try to remove after paging implementation
            isReturnedFromPost = true
        }
        stateViewFlipperFeedChild.setRetryMethod { callOperations() }
    }

    override fun onBindViewModel() = with(viewModel) {
        postsLiveData.observe { data ->
            postsAdapter.submitData(lifecycle, data)
        }

        postsLoadingStateLiveData.observe { result ->
            if (isFirstLoading) {
                binding.stateViewFlipperFeedChild.setStateFromResult(result)
                result.doOnSuccess { isFirstLoading = false }
            } else if (isRefreshingBySwipe) {
                binding.swipeRefreshLayoutFeedChild.isRefreshing = result.isLoading
                result.doOnComplete { isRefreshingBySwipe = false }
                result.doOnFailure { error ->
                    showToast(error.title)
                    Timber.e(error.title)
                }
            }
        }

        openPostLiveEvent.observe { postId ->
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostFragment(postId))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (parentFragment as? OnLoadingStateChangedListener)?.let {
            onLoadingStateChangedListener = it
        }
    }

    private fun setupPostsList() = with(binding.recyclerViewFeedChildPosts) {
        adapter = postsAdapter.apply {
            onPostClick = viewModel::openPost
            addLoadStateListener(viewModel::bindLoadingState)
        }.withLoadStateFooter(PagingLoadStateAdapter { postsAdapter.retry() })
        addVerticalDividerItemDecoration()
    }
}
