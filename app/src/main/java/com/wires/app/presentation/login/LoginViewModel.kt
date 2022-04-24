package com.wires.app.presentation.login

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.usecase.auth.LoginUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val loginLiveEvent: LiveData<LoadableResult<Unit>> = _loginLiveEvent

    fun login(email: String, password: String) {
        _loginLiveEvent.launchLoadData(loginUseCase.executeLoadable(LoginUseCase.Params(email, password)))
    }
}
