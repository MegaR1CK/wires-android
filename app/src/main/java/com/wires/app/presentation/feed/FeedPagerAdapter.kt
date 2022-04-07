package com.wires.app.presentation.feed

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.ViewFeedTabBinding

class FeedPagerAdapter(
    private val fragment: Fragment,
    private val pageCount: Int,
    private val createFragment: (Int) -> Fragment
) : FragmentStateAdapter(fragment) {

    lateinit var interests: List<UserInterest>
    lateinit var userInterests: List<UserInterest>

    override fun getItemCount() = pageCount

    override fun createFragment(position: Int): Fragment = createFragment.invoke(position)

    fun getTabView(position: Int): View {
        val tabBinding = ViewFeedTabBinding.inflate(fragment.layoutInflater)
        tabBinding.textViewTab.text = when {
            userInterests.isEmpty() -> interests[position].value
            else -> if (position == 0) fragment.getString(R.string.feed_first_tab_text) else interests[position - 1].value
        }
        return tabBinding.root
    }
}
