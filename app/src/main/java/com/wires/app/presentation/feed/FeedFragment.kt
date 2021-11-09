package com.wires.app.presentation.feed

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.toInt
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.feed.feedchild.FeedChildFragment
import com.wires.app.presentation.feed.feedchild.OnLoadingCompleteListener
import timber.log.Timber

class FeedFragment : BaseFragment(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by appViewModels()
    private val binding by viewBinding(FragmentFeedBinding::bind)

    override val showBottomNavigationView = true

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
                binding.toolbarFeed.title = getString(R.string.feed_title, user.firstName)
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
        ) { position ->
            val fragment = when {
                interests.isEmpty() -> FeedChildFragment(listOf(UserInterest.values()[position]))
                position == 0 -> FeedChildFragment(interests)
                else -> FeedChildFragment(listOf(UserInterest.values()[position - 1]))
            }
            fragment.onLoadingCompleteListener = object : OnLoadingCompleteListener {
                override fun onLoadingComplete() {
                    appBarLayoutFeed.setExpanded(true)
                }
            }
            fragment
        }
        pagerAdapter.userInterests = interests
        pagerAdapter.interests = UserInterest.values().toList()
        viewPagerFeed.adapter = pagerAdapter
        TabLayoutMediator(tabLayoutFeed, viewPagerFeed) { tab, position ->
            tab.customView = pagerAdapter.getTabView(position)
        }.attach()
    }
}
