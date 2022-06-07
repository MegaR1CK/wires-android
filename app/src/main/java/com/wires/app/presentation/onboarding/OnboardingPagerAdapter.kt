package com.wires.app.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(
    fragment: Fragment,
    private val pageCount: Int,
    private val createFragment: (Int) -> Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = pageCount

    override fun createFragment(position: Int) = createFragment.invoke(position)
}
