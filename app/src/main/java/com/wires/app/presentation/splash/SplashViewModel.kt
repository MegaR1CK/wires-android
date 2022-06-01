package com.wires.app.presentation.splash

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import kotlinx.coroutines.delay
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    companion object {
        private const val SPLASH_MIN_DELAY = 2500L
    }

    private val _initLiveEvent = SingleLiveEvent<LoadableResult<SplashResult>>()
    val initLiveEvent: LiveData<LoadableResult<SplashResult>> = _initLiveEvent

    private val _navigateToLoginLiveEvent = SingleLiveEvent<Unit>()
    val navigateToLoginLiveEvent: LiveData<Unit> = _navigateToLoginLiveEvent

    private val _navigateToRegisterLiveEvent = SingleLiveEvent<Unit>()
    val navigateToRegisterLiveEvent: LiveData<Unit> = _navigateToRegisterLiveEvent

    fun runIntroFlow() {
        _initLiveEvent.launchLoadData {
            delay(SPLASH_MIN_DELAY)
            if (userRepository.isSignedIn) {
                SplashResult.MAIN_SCREEN
            } else {
                SplashResult.AUTH
            }
        }
    }

    fun navigateToLogin() {
        _navigateToLoginLiveEvent.postValue(Unit)
    }

    fun navigateToRegister() {
        _navigateToRegisterLiveEvent.postValue(Unit)
    }

    enum class SplashResult {
        AUTH, MAIN_SCREEN
    }
}
