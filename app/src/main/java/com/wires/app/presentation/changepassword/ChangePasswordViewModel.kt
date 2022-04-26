package com.wires.app.presentation.changepassword

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.usecase.user.ChangeUserPasswordUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val changeUserPasswordUseCase: ChangeUserPasswordUseCase
) : BaseViewModel() {

    private val _changePasswordLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val changePasswordLiveEvent: LiveData<LoadableResult<Unit>> = _changePasswordLiveEvent

    fun changePassword(oldPassword: String, newPassword: String) {
        _changePasswordLiveEvent.launchLoadData(
            changeUserPasswordUseCase.executeLoadable(ChangeUserPasswordUseCase.Params(oldPassword, newPassword))
        )
    }
}
