package com.wires.app.presentation.feed.feedchild

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.getColorAttribute
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.createpost.CreatePostFragment
import com.wires.app.presentation.feed.FeedFragmentDirections
import com.wires.app.presentation.post.PostFragment
import timber.log.Timber
import javax.inject.Inject

class FeedChildFragment(private val interests: List<UserInterest>) : BaseFragment(R.layout.fragment_feed_child) {

    companion object {
        fun newInstance(interests: List<UserInterest>): FeedChildFragment {
            return FeedChildFragment(interests)
        }
    }

    private val viewModel: FeedChildViewModel by appViewModels()
    private val binding by viewBinding(FragmentFeedChildBinding::bind)

    @Inject lateinit var postsAdapter: PostsAdapter

    private var onLoadingStateChangedListener: OnLoadingStateChangedListener? = null
    private var isReturnedFromPost = false

    override fun callOperations() {
        viewModel.getPosts(interests)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        setupPostsList()
        swipeRefreshLayoutFeedChild.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
        swipeRefreshLayoutFeedChild.setOnRefreshListener { callOperations() }
        parentFragment?.setFragmentResultListener(CreatePostFragment.POST_CREATED_RESULT_KEY) { _, _ ->
            viewModel.clearPosts()
            callOperations()
        }
        parentFragment?.setFragmentResultListener(PostFragment.POST_RETURN_KEY) { _, _ ->
            // TODO: try to remove after paging implementation
            isReturnedFromPost = true
        }
        stateViewFlipperFeedChild.setRetryMethod { callOperations() }
    }

    override fun onBindViewModel() = with(viewModel) {
        postsLiveData.observe { result ->
            (result.isLoading && postsAdapter.itemCount != 0).let { isNotFirstLoading ->
                binding.swipeRefreshLayoutFeedChild.isRefreshing = isNotFirstLoading
                if (!isNotFirstLoading) binding.stateViewFlipperFeedChild.setStateFromResult(result)
            }
            result.doOnSuccess { items ->
                postsAdapter.submitList(items)
                if (!isReturnedFromPost) {
                    binding.recyclerViewFeedChildPosts.scrollToPosition(0)
                    isReturnedFromPost = false
                }
                onLoadingStateChangedListener?.onLoadingStateChanged(result)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
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
            onPostClick = { postId ->
                viewModel.openPost(postId)
            }
        }
        addVerticalDividerItemDecoration()
    }
}
