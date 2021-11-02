package com.wires.app.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wires.app.databinding.FragmentOnboardingBinding
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.presentation.base.BaseFragment

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private val viewModel: OnboardingViewModel by appViewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardingBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentOnboardingBinding.inflate(inflater, viewGroup, attachToRoot)
        }

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
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment())
        }
        navigateToRegisterLiveEvent.observe {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToRegisterFragment())
        }
    }
}
