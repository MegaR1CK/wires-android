package com.wires.app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wires.app.di.util.ViewModelFactory
import com.wires.app.di.util.ViewModelKey
import com.wires.app.presentation.changepassword.ChangePasswordViewModel
import com.wires.app.presentation.channels.ChannelsViewModel
import com.wires.app.presentation.chat.ChatViewModel
import com.wires.app.presentation.createchannel.CreateChannelViewModel
import com.wires.app.presentation.createpost.CreatePostViewModel
import com.wires.app.presentation.edituser.EditUserViewModel
import com.wires.app.presentation.feed.FeedViewModel
import com.wires.app.presentation.feed.feedchild.FeedChildViewModel
import com.wires.app.presentation.login.LoginViewModel
import com.wires.app.presentation.onboarding.OnboardingViewModel
import com.wires.app.presentation.onboarding.interestssetup.InterestsSetupViewModel
import com.wires.app.presentation.onboarding.usersetup.UserSetupViewModel
import com.wires.app.presentation.pickusers.PickUsersViewModel
import com.wires.app.presentation.post.PostViewModel
import com.wires.app.presentation.profile.ProfileViewModel
import com.wires.app.presentation.register.RegisterViewModel
import com.wires.app.presentation.settings.SettingsViewModel
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
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun registerViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel::class)
    abstract fun onboardingViewModel(viewModel: OnboardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserSetupViewModel::class)
    abstract fun userSetupViewModel(viewModel: UserSetupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InterestsSetupViewModel::class)
    abstract fun interestsSetupViewModel(viewModel: InterestsSetupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun feedViewModel(viewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedChildViewModel::class)
    abstract fun feedChildViewModel(viewModel: FeedChildViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun postViewModel(viewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePostViewModel::class)
    abstract fun createPostViewModel(viewModel: CreatePostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChannelsViewModel::class)
    abstract fun channelsViewModel(viewModel: ChannelsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun chatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditUserViewModel::class)
    abstract fun editUserViewModel(viewModel: EditUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun changePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateChannelViewModel::class)
    abstract fun createChannelViewModel(viewModel: CreateChannelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PickUsersViewModel::class)
    abstract fun pickUsersViewModel(viewModel: PickUsersViewModel): ViewModel
}
