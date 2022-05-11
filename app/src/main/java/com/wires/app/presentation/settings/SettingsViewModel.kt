package com.wires.app.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.usecase.auth.LogoutUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase
) : BaseViewModel() {

    private val _openChangePasswordLiveEvent = SingleLiveEvent<Unit>()
    val openChangePasswordLiveEvent: LiveData<Unit> = _openChangePasswordLiveEvent

    private val _logoutLiveEvent = SingleLiveEvent<Unit>()
    val logoutLiveEvent: LiveData<Unit> = _logoutLiveEvent

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _openMailClientLiveEvent = SingleLiveEvent<UserWrapper>()
    val openMailClientLiveEvent: LiveData<UserWrapper> = _openMailClientLiveEvent

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun openMailClient() {
        _openMailClientLiveEvent.postValue(_userLiveData.value?.getOrNull())
    }

    fun openChangePassword() {
        _openChangePasswordLiveEvent.postValue(Unit)
    }

    fun logout() {
        _logoutLiveEvent.postValue(logoutUseCase.execute(Unit))
    }
}
