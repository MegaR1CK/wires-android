package com.wires.app.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.usecase.auth.LoginUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import com.wires.app.presentation.utils.Validator
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validator: Validator
) : BaseViewModel() {

    private val _emailErrorLiveData = MutableLiveData<Int?>()
    val emailErrorLiveData: LiveData<Int?> = _emailErrorLiveData

    private val _passwordErrorLiveData = MutableLiveData<Int?>()
    val passwordErrorLiveData: LiveData<Int?> = _passwordErrorLiveData

    private val _loginLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val loginLiveEvent: LiveData<LoadableResult<Unit>> = _loginLiveEvent

    fun validateEmail(email: String) {
        _emailErrorLiveData.value = validator.validateEmail(email)
    }

    fun validatePassword(password: String) {
        _passwordErrorLiveData.value = validator.validatePassword(password, patternValidation = false)
    }

    fun login(email: String, password: String) {
        validateEmail(email)
        validatePassword(password)
        if (_emailErrorLiveData.value != null || _passwordErrorLiveData.value != null) return
        _loginLiveEvent.launchLoadData(loginUseCase.executeLoadable(LoginUseCase.Params(email, password)))
    }
}
