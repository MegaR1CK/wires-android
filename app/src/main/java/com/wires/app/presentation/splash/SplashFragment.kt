package com.wires.app.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wires.app.databinding.FragmentSplashBinding
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.presentation.base.BaseFragment

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by appViewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentSplashBinding.inflate(inflater, viewGroup, attachToRoot)
        }

    override fun callOperations() {
        viewModel.runIntroFlow()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(requireNotNull(binding)) {
        root.fitTopAndBottomInsetsWithPadding()
    }

    override fun onBindViewModel() = with(viewModel) {
        initLiveEvent.observe { result ->
            result.doOnSuccess {
                when (it) {
                    SplashViewModel.SplashResult.MAIN_SCREEN -> {
                    }
                    SplashViewModel.SplashResult.AUTH -> {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthGraph())
                    }
                }
            }
        }
    }
}
