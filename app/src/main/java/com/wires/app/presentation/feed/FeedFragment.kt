package com.wires.app.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.toInt
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.feed.feedchild.FeedChildFragment
import timber.log.Timber

class FeedFragment : BaseFragment<FragmentFeedBinding>() {

    private val viewModel: FeedViewModel by appViewModels()
    override val showBottomNavigationView = true
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeedBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentFeedBinding.inflate(inflater, viewGroup, attachToRoot)
        }

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
    }

    override fun onBindViewModel() = with(viewModel) {
        userData.observe { result ->
            binding.stateViewFlipperFeed.setStateFromResult(result)
            result.doOnSuccess { user ->
                binding.toolbarFeed.title = getString(R.string.feed_title, user.firstName ?: user.username)
                setupPager(user.interests.orEmpty())
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }

    private fun setupPager(interests: List<UserInterest>) = with(binding) {
        val pagerAdapter = FeedPagerAdapter(
            this@FeedFragment,
            UserInterest.values().size + interests.isNotEmpty().toInt()
        ) {
            FeedChildFragment.newInstance()
        }
        pagerAdapter.userInterests = interests
        pagerAdapter.interests = UserInterest.values().toList()
        viewPagerFeed.adapter = pagerAdapter
        TabLayoutMediator(tabLayoutFeed, viewPagerFeed) { tab, position ->
            tab.customView = pagerAdapter.getTabView(position)
        }.attach()
    }
}
