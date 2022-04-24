package com.wires.app.presentation.settings

import androidx.lifecycle.LiveData
import com.wires.app.domain.usecase.auth.LogoutUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {

    private val _logoutLiveEvent = SingleLiveEvent<Unit>()
    val logoutLiveEvent: LiveData<Unit> = _logoutLiveEvent

    fun logout() {
        _logoutLiveEvent.postValue(logoutUseCase.execute(Unit))
    }
}
