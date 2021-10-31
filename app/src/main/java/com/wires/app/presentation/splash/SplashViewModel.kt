package com.wires.app.presentation.splash

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.repository.TokenRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    companion object {
        private const val SPLASH_MIN_DELAY = 1500L
    }

    private val _initLiveEvent = SingleLiveEvent<LoadableResult<SplashResult>>()
    val initLiveEvent: LiveData<LoadableResult<SplashResult>> = _initLiveEvent

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

    enum class SplashResult {
        AUTH, MAIN_SCREEN
    }
}