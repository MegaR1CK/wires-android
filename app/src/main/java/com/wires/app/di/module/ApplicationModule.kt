package com.wires.app.di.module

import android.app.Application
import android.content.Context
import com.wires.app.data.DiffUtilCallbackFactory
import com.wires.app.data.DiffUtilItemCallbackFactory
import com.wires.app.WiresApplication
import dagger.Module
import dagger.Provides

@Module
open class ApplicationModule {

    @Provides
    fun provideContext(app: WiresApplication): Context {
        return app.applicationContext
    }

    @Provides
    fun provideApplication(app: WiresApplication): Application {
        return app
    }

    @Provides
    fun provideDiffUtilItemCallbackFactory(): DiffUtilItemCallbackFactory {
        return DiffUtilItemCallbackFactory()
    }

    @Provides
    fun provideDiffUtilCallbackFactory(
        diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory
    ): DiffUtilCallbackFactory {
        return DiffUtilCallbackFactory(diffUtilItemCallbackFactory)
    }
}
