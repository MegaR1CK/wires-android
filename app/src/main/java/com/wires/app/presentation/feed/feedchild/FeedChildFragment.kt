package com.wires.app.presentation.feed.feedchild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.extensions.addLinearSpaceItemDecoration
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.getColorAttribute
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.feed.FeedFragmentDirections
import timber.log.Timber
import javax.inject.Inject

class FeedChildFragment(private val interests: List<UserInterest>) : BaseFragment<FragmentFeedChildBinding>() {

    companion object {
        fun newInstance(interests: List<UserInterest>): FeedChildFragment {
            return FeedChildFragment(interests)
        }
    }

    private val viewModel: FeedChildViewModel by appViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeedChildBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentFeedChildBinding.inflate(inflater, viewGroup, attachToRoot)
        }

    @Inject lateinit var postsAdapter: PostsAdapter

    var onLoadingCompleteListener: OnLoadingCompleteListener? = null

    override fun callOperations() {
        viewModel.getPosts(interests)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        setupPostsList()
        swipeRefreshLayoutFeedChild.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
        swipeRefreshLayoutFeedChild.setOnRefreshListener {
            callOperations()
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        postsLiveData.observe { result ->
            (result.isLoading && binding.recyclerViewFeedChildPosts.adapter?.itemCount != 0).let {
                binding.swipeRefreshLayoutFeedChild.isRefreshing = it
                if (!it) binding.stateViewFlipperFeedChild.setStateFromResult(result)
            }
            result.doOnSuccess { items ->
                postsAdapter.submitList(items)
                binding.recyclerViewFeedChildPosts.scrollToPosition(0)
                onLoadingCompleteListener?.onLoadingComplete()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        openPostLiveEvent.observe { postId ->
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostFragment(postId))
        }
    }

    private fun setupPostsList() = with(binding.recyclerViewFeedChildPosts) {
        adapter = postsAdapter
        postsAdapter.onPostClick = { postId ->
            viewModel.openPost(postId)
        }
        addLinearSpaceItemDecoration(R.dimen.feed_post_spacing)
        addVerticalDividerItemDecoration()
    }
}
