package com.wires.app.di.module

import com.wires.app.presentation.feed.FeedFragment
import com.wires.app.presentation.feed.feedchild.FeedChildFragment
import com.wires.app.presentation.login.LoginFragment
import com.wires.app.presentation.onboarding.OnboardingFragment
import com.wires.app.presentation.register.RegisterFragment
import com.wires.app.presentation.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun onboardingFragment(): OnboardingFragment

    @ContributesAndroidInjector
    abstract fun loginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun registerFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun feedFragment(): FeedFragment

    @ContributesAndroidInjector
    abstract fun feedChildFragment(): FeedChildFragment
}
