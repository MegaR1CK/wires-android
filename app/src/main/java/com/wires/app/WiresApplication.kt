package com.wires.app

import com.wires.app.data.remote.DEFAULT_ERROR_MESSAGE
import com.wires.app.data.remote.DEFAULT_ERROR_TITLE
import com.wires.app.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.paperdb.Paper
import timber.log.Timber

class WiresApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .factory()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Paper.init(applicationContext)
        initErrorStrings()
    }

    private fun initErrorStrings() = with(applicationContext) {
        DEFAULT_ERROR_TITLE = getString(R.string.error_no_network_title)
        DEFAULT_ERROR_MESSAGE = getString(R.string.error_no_network_description)
    }
}
