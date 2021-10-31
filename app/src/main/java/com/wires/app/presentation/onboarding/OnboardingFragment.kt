package com.wires.app.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.databinding.FragmentOnboardingBinding
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.presentation.base.BaseFragment

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardingBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentOnboardingBinding.inflate(inflater, viewGroup, attachToRoot)
        }

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(requireNotNull(binding)) {
        root.fitTopAndBottomInsetsWithPadding()
    }

    override fun onBindViewModel() = Unit
}
