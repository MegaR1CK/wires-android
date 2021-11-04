package com.wires.app.presentation.feed.feedchild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.databinding.FragmentFeedChildBinding
import com.wires.app.presentation.base.BaseFragment

class FeedChildFragment : BaseFragment<FragmentFeedChildBinding>() {

    companion object {
        fun newInstance(): FeedChildFragment {
            return FeedChildFragment()
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeedChildBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentFeedChildBinding.inflate(inflater, viewGroup, attachToRoot)
        }

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = Unit

    override fun onBindViewModel() = Unit
}
