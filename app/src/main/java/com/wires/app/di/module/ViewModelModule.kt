package com.wires.app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wires.app.di.util.ViewModelFactory
import com.wires.app.di.util.ViewModelKey
import com.wires.app.presentation.onboarding.OnboardingViewModel
import com.wires.app.presentation.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel::class)
    abstract fun onboardingViewModel(viewModel: OnboardingViewModel): ViewModel
}
