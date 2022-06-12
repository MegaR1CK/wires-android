package com.wires.app.presentation.splash

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.usecase.SplashUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val splashUseCase: SplashUseCase
) : BaseViewModel() {

    private val _initLiveEvent = SingleLiveEvent<LoadableResult<SplashResult>>()
    val initLiveEvent: LiveData<LoadableResult<SplashResult>> = _initLiveEvent

    private val _navigateToLoginLiveEvent = SingleLiveEvent<Unit>()
    val navigateToLoginLiveEvent: LiveData<Unit> = _navigateToLoginLiveEvent

    private val _navigateToRegisterLiveEvent = SingleLiveEvent<Unit>()
    val navigateToRegisterLiveEvent: LiveData<Unit> = _navigateToRegisterLiveEvent

    fun runIntroFlow() {
        _initLiveEvent.launchLoadData(splashUseCase.executeLoadable(Unit))
    }

    fun navigateToLogin() {
        _navigateToLoginLiveEvent.postValue(Unit)
    }

    fun navigateToRegister() {
        _navigateToRegisterLiveEvent.postValue(Unit)
    }
}
