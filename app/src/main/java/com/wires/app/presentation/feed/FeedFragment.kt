package com.wires.app.presentation.feed

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.toInt
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.feed.feedchild.FeedChildFragment
import com.wires.app.presentation.feed.feedchild.OnLoadingStateChangedListener
import timber.log.Timber

class FeedFragment : BaseFragment(R.layout.fragment_feed), OnLoadingStateChangedListener {

    private val viewModel: FeedViewModel by appViewModels()
    private val binding by viewBinding(FragmentFeedBinding::bind)

    override val showBottomNavigationView = true

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        buttonFeedCreatePost.setOnClickListener { viewModel.openCreatePost() }
    }

    override fun onBindViewModel() = with(viewModel) {
        userData.observe { result ->
            binding.stateViewFlipperFeed.setStateFromResult(result)
            binding.appBarLayoutFeed.isVisible = result.isSuccess
            result.doOnSuccess { user ->
                binding.toolbarFeed.title = getString(R.string.feed_title, user.firstName ?: user.username)
                setupPager(user.interests.orEmpty())
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        createPostLiveEvent.observe {
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToCreatePostFragment())
        }
    }

    override fun onLoadingStateChanged(state: LoadableResult<*>) {
        binding.buttonFeedCreatePost.isVisible = state.isSuccess
    }

    private fun setupPager(interests: List<UserInterest>) = with(binding) {
        val pagerAdapter = FeedPagerAdapter(
            this@FeedFragment,
            UserInterest.values().size + interests.isNotEmpty().toInt()
        ) { position ->
            when {
                interests.isEmpty() -> FeedChildFragment.newInstance(UserInterest.values()[position])
                position == 0 -> FeedChildFragment.newInstance(null)
                else -> FeedChildFragment.newInstance(UserInterest.values()[position - 1])
            }
        }
        pagerAdapter.userInterests = interests
        pagerAdapter.interests = UserInterest.values().toList()
        viewPagerFeed.adapter = pagerAdapter
        TabLayoutMediator(tabLayoutFeed, viewPagerFeed) { tab, position ->
            tab.customView = pagerAdapter.getTabView(position)
        }.attach()
    }
}
