package com.wires.app.presentation.splash

import android.os.Bundle
import com.wires.app.R
import com.wires.app.databinding.FragmentSplashBinding
import com.wires.app.extensions.setFitSystemWindowPadding
import com.wires.app.presentation.base.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by appViewModels()
    private lateinit var binding: FragmentSplashBinding

    override fun callOperations() {
        viewModel.runIntroFlow()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        with(binding) {
            root.setFitSystemWindowPadding(bottomInsetSet = true)
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        initLiveEvent.observe { result ->
            result.doOnSuccess {
                when (it) {
                    SplashViewModel.SplashResult.MAIN_SCREEN -> {

                    }
                    SplashViewModel.SplashResult.AUTH -> {

                    }
                }
            }
        }
    }
}