package com.wires.app.presentation.feed

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentFeedBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.toInt
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.createpost.CreatePostFragment
import com.wires.app.presentation.feed.feedchild.FeedChildFragment
import com.wires.app.presentation.feed.feedchild.OnLoadingCompleteListener
import timber.log.Timber

class FeedFragment : BaseFragment(R.layout.fragment_feed), OnLoadingCompleteListener {

    companion object {
        private const val VIEWPAGER_FRAGMENT_PREFIX = "f"
    }

    private val viewModel: FeedViewModel by appViewModels()
    private val binding by viewBinding(FragmentFeedBinding::bind)

    override val showBottomNavigationView = true

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        buttonFeedCreatePost.setOnClickListener { viewModel.openCreatePost() }
        setFragmentResultListener(CreatePostFragment.POST_CREATED_RESULT_KEY) { _, _ ->
            val currentFragment = childFragmentManager
                .findFragmentByTag("$VIEWPAGER_FRAGMENT_PREFIX${viewPagerFeed.currentItem}") as? FeedChildFragment
            currentFragment?.refreshFragment()
        }
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

        createPostLiveEvent.observe {
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToCreatePostFragment())
        }
    }

    override fun onLoadingComplete() {
        binding.appBarLayoutFeed.setExpanded(true)
        binding.buttonFeedCreatePost.isVisible = true
    }

    private fun setupPager(interests: List<UserInterest>) = with(binding) {
        val pagerAdapter = FeedPagerAdapter(
            this@FeedFragment,
            UserInterest.values().size + interests.isNotEmpty().toInt()
        ) { position ->
            when {
                interests.isEmpty() -> FeedChildFragment(listOf(UserInterest.values()[position]))
                position == 0 -> FeedChildFragment(interests)
                else -> FeedChildFragment(listOf(UserInterest.values()[position - 1]))
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
