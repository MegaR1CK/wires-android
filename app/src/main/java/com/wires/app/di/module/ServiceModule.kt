package com.wires.app.di.module

import com.wires.app.managers.WiresFirebaseMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun firebaseMessagingService(): WiresFirebaseMessagingService
}
