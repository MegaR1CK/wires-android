package com.wires.app.di.module

import com.wires.app.presentation.changepassword.ChangePasswordFragment
import com.wires.app.presentation.channels.ChannelsFragment
import com.wires.app.presentation.chat.ChatFragment
import com.wires.app.presentation.createpost.CreatePostFragment
import com.wires.app.presentation.edituser.EditUserFragment
import com.wires.app.presentation.feed.FeedFragment
import com.wires.app.presentation.feed.feedchild.FeedChildFragment
import com.wires.app.presentation.login.LoginFragment
import com.wires.app.presentation.onboarding.OnboardingFragment
import com.wires.app.presentation.post.PostFragment
import com.wires.app.presentation.profile.ProfileFragment
import com.wires.app.presentation.register.RegisterFragment
import com.wires.app.presentation.settings.SettingsFragment
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

    @ContributesAndroidInjector
    abstract fun postFragment(): PostFragment

    @ContributesAndroidInjector
    abstract fun createPostFragment(): CreatePostFragment

    @ContributesAndroidInjector
    abstract fun channelsFragment(): ChannelsFragment

    @ContributesAndroidInjector
    abstract fun chatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun profileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun editUserFragment(): EditUserFragment

    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun changePasswordFragment(): ChangePasswordFragment
}
