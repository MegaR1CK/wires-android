package com.wires.app.presentation.onboarding

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentOnboardingBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getDrawableCompat
import com.wires.app.extensions.getKeyboardInset
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.onboarding.interestssetup.InterestsSetupFragment
import com.wires.app.presentation.onboarding.usersetup.UserSetupFragment
import timber.log.Timber

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding), OnSubmitListener {

    companion object {
        private const val ONBOARDING_PAGES_COUNT = 2
    }

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val viewModel: OnboardingViewModel by appViewModels()

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            viewOnboardingIndicator.isVisible = insets.getKeyboardInset() == 0
        }
        toolbarOnboarding.setNavigationOnClickListener {
            val currentItem = viewPagerOnboarding.currentItem
            if (currentItem != 0) viewPagerOnboarding.currentItem = currentItem.dec()
        }
        buttonOnboardingSkip.setOnClickListener { viewModel.skipOnboarding() }
        setupPager()
    }

    override fun onBindViewModel() = with(viewModel) {
        updateUserLiveEvent.observe { result ->
            binding.viewPagerOnboarding.adapter?.let { adapter ->
                val lastFragment =
                    findPagerFragmentAtPosition(childFragmentManager, adapter.itemCount - 1) as? InterestsSetupFragment
                lastFragment?.setButtonState(result)
            }
            result.doOnSuccess {
                navigateTo(OnboardingFragmentDirections.actionOnboardingFragmentToFeedGraph())
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
        skipOnboardingLiveEvent.observe {
            navigateTo(OnboardingFragmentDirections.actionOnboardingFragmentToFeedGraph())
        }
    }

    override fun onSubmitUserData(userSetupData: UserSetupData) {
        viewModel.userData = userSetupData
        requireActivity().hideSoftKeyboard(binding.viewPagerOnboarding)
        with(binding.viewPagerOnboarding) {
            if (currentItem != adapter?.itemCount) currentItem = currentItem.inc()
        }
    }

    override fun onSubmitInterests(interests: List<UserInterest>) {
        viewModel.interests = interests
        viewModel.setUserData()
    }

    private fun setupPager() = with(binding) {
        val pagerAdapter = OnboardingPagerAdapter(
            fragment = this@OnboardingFragment,
            pageCount = ONBOARDING_PAGES_COUNT
        ) { position ->
            if (position == 0) UserSetupFragment() else InterestsSetupFragment()
        }
        viewOnboardingIndicator.count = pagerAdapter.itemCount
        with(viewPagerOnboarding) {
            offscreenPageLimit = 1
            isUserInputEnabled = false
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewOnboardingIndicator.selection = position
                    toolbarOnboarding.navigationIcon = requireContext()
                        .getDrawableCompat(R.drawable.ic_arrow_back)
                        .takeIf { position == pagerAdapter.itemCount - 1 }
                }
            })
        }
    }

    private fun findPagerFragmentAtPosition(fragmentManager: FragmentManager, position: Int): Fragment? {
        return fragmentManager.findFragmentByTag("f$position")
    }
}
