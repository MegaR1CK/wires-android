package com.wires.app.presentation.splash

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentSplashBinding
import com.wires.app.extensions.fitTopAndBottomInsetsWithPadding
import com.wires.app.extensions.navigateTo
import com.wires.app.presentation.base.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        private const val LOGO_TRANSITION_ANIMATION_DURATION = 500L
        private const val TARGET_LOGO_VERTICAL_BIAS = 0.1f
    }

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel: SplashViewModel by appViewModels()
    private val args: SplashFragmentArgs by navArgs()

    private var animationFinished = false

    override fun callOperations() {
        viewModel.runIntroFlow()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopAndBottomInsetsWithPadding()

        buttonSplashLogin.setOnClickListener {
            viewModel.navigateToLogin()
        }
        buttonSplashRegister.setOnClickListener {
            viewModel.navigateToRegister()
        }

        if (animationFinished || args.needSkipAnimation) {
            (linearLayoutSplash.layoutParams as? ConstraintLayout.LayoutParams)?.verticalBias = TARGET_LOGO_VERTICAL_BIAS
            listOf(buttonSplashLogin, buttonSplashRegister).forEach { button ->
                button.alpha = 1f
                button.isVisible = true
            }
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        initLiveEvent.observe { result ->
            result.doOnSuccess {
                when (it) {
                    SplashResult.MAIN_SCREEN -> {
                        navigateTo(SplashFragmentDirections.actionSplashFragmentToFeedGraph())
                    }
                    SplashResult.AUTH -> {
                        if (!args.needSkipAnimation) startLogoAnimation()
                    }
                }
            }
        }
        navigateToLoginLiveEvent.observe {
            navigateTo(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
        navigateToRegisterLiveEvent.observe {
            navigateTo(SplashFragmentDirections.actionSplashFragmentToRegisterFragment())
        }
    }

    private fun startLogoAnimation() = with(binding) {
        TransitionManager.beginDelayedTransition(
            root,
            AutoTransition().apply { duration = LOGO_TRANSITION_ANIMATION_DURATION }
        )
        val linearLayoutParams = linearLayoutSplash.layoutParams as? ConstraintLayout.LayoutParams
        linearLayoutParams?.verticalBias = TARGET_LOGO_VERTICAL_BIAS
        linearLayoutSplash.layoutParams = linearLayoutParams
        AnimatorSet().apply {
            addListener(
                onStart = {
                    buttonSplashLogin.isVisible = true
                    buttonSplashRegister.isVisible = true
                },
                onEnd = { animationFinished = true }
            )
            playTogether(
                getViewAlphaAnimator(buttonSplashLogin),
                getViewAlphaAnimator(buttonSplashRegister)
            )
            startDelay = LOGO_TRANSITION_ANIMATION_DURATION
        }.start()
    }

    private fun getViewAlphaAnimator(view: View) =
        AnimatorInflater.loadAnimator(requireContext(), R.animator.anim_button_alpha).apply {
            setTarget(view)
        }
}
