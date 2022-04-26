package com.wires.app.presentation.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.usecase.user.ChangeUserPasswordUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val changeUserPasswordUseCase: ChangeUserPasswordUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase
) : BaseViewModel() {

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _changePasswordLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val changePasswordLiveEvent: LiveData<LoadableResult<Unit>> = _changePasswordLiveEvent

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        _userLiveData.value?.getOrNull()?.user?.email?.let { mail ->
            _changePasswordLiveEvent.launchLoadData(
                changeUserPasswordUseCase.executeLoadable(ChangeUserPasswordUseCase.Params(mail, oldPassword, newPassword))
            )
        }
    }
}
