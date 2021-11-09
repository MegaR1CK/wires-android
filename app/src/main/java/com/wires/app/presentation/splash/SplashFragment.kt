package com.wires.app.presentation.splash

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentSplashBinding
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.presentation.base.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by appViewModels()
    private val binding by viewBinding(FragmentSplashBinding::bind)

    override fun callOperations() {
        viewModel.runIntroFlow()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopAndBottomInsetsWithPadding()
    }

    override fun onBindViewModel() = with(viewModel) {
        initLiveEvent.observe { result ->
            result.doOnSuccess {
                when (it) {
                    SplashViewModel.SplashResult.MAIN_SCREEN -> {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToFeedGraph())
                    }
                    SplashViewModel.SplashResult.AUTH -> {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthGraph())
                    }
                }
            }
        }
    }
}
