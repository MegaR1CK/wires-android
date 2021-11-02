package com.wires.app.presentation.onboarding

import androidx.lifecycle.LiveData
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class OnboardingViewModel @Inject constructor() : BaseViewModel() {

    private val _navigateToLoginLiveEvent = SingleLiveEvent<Unit>()
    val navigateToLoginLiveEvent: LiveData<Unit> = _navigateToLoginLiveEvent

    private val _navigateToRegisterLiveEvent = SingleLiveEvent<Unit>()
    val navigateToRegisterLiveEvent: LiveData<Unit> = _navigateToRegisterLiveEvent

    fun navigateToLogin() {
        _navigateToLoginLiveEvent.postValue(Unit)
    }

    fun navigateToRegister() {
        _navigateToRegisterLiveEvent.postValue(Unit)
    }
}
