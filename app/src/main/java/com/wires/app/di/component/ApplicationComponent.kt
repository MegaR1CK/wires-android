package com.wires.app.di.component

import com.wires.app.WiresApplication
import com.wires.app.di.module.ActivityModule
import com.wires.app.di.module.ApiServiceModule
import com.wires.app.di.module.ApplicationModule
import com.wires.app.di.module.FragmentModule
import com.wires.app.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        ApiServiceModule::class,
        ApplicationModule::class,
        FragmentModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<WiresApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: WiresApplication): ApplicationComponent
    }
}
