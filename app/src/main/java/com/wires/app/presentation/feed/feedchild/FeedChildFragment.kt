package com.wires.app.presentation.feed.feedchild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.extensions.addLinearSpaceItemDecoration
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.presentation.base.BaseFragment
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

    override fun callOperations() {
        viewModel.getPosts(interests)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopAndBottomInsetsWithPadding()
        setupPostsList()
    }

    override fun onBindViewModel() = with(viewModel) {
        postsLiveData.observe { result ->
            binding.stateViewFlipperFeedChild.setStateFromResult(result)
            result.doOnSuccess { items ->
                postsAdapter.submitList(items)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }

    private fun setupPostsList() = with(binding.recyclerViewFeedChildPosts) {
        adapter = postsAdapter
        addLinearSpaceItemDecoration(R.dimen.feed_post_spacing)
        addVerticalDividerItemDecoration()
    }
}
