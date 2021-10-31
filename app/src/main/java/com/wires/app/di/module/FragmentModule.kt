package com.wires.app.di.module

import com.wires.app.presentation.onboarding.OnboardingFragment
import com.wires.app.presentation.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun onboardingFragment(): OnboardingFragment
}
