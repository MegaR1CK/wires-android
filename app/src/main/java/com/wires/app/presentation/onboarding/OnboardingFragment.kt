package com.wires.app.presentation.onboarding

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentOnboardingBinding
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.extensions.navigateTo
import com.wires.app.presentation.base.BaseFragment

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val viewModel: OnboardingViewModel by appViewModels()
    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopAndBottomInsetsWithPadding()
        buttonOnboardingLogin.setOnClickListener {
            viewModel.navigateToLogin()
        }
        buttonOnboardingRegister.setOnClickListener {
            viewModel.navigateToRegister()
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        navigateToLoginLiveEvent.observe {
            navigateTo(OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment())
        }
        navigateToRegisterLiveEvent.observe {
            navigateTo(OnboardingFragmentDirections.actionOnboardingFragmentToRegisterFragment())
        }
    }
}
