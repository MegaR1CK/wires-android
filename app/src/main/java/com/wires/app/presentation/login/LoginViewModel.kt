package com.wires.app.presentation.login

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.domain.Cryptor
import com.wires.app.domain.repository.AuthRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import com.wires.app.presentation.utils.Validator
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceStorage: PreferenceStorage
) : BaseViewModel() {

    private val _emailErrorLiveData = SingleLiveEvent<Int?>()
    val emailErrorLiveData: LiveData<Int?> = _emailErrorLiveData

    private val _passwordErrorLiveData = SingleLiveEvent<Int?>()
    val passwordErrorLiveData: LiveData<Int?> = _passwordErrorLiveData

    private val _loginLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val loginLiveEvent: LiveData<LoadableResult<Unit>> = _loginLiveEvent

    @Inject lateinit var validator: Validator
    @Inject lateinit var cryptor: Cryptor

    fun validateEmail(email: String) {
        _emailErrorLiveData.postValue(validator.validateEmail(email))
    }

    fun validatePassword(password: String) {
        _passwordErrorLiveData.postValue(validator.validatePassword(password))
    }

    fun login(email: String, password: String) {
        validateEmail(email)
        validatePassword(password)
        if (_emailErrorLiveData.value != null || _passwordErrorLiveData.value != null) return
        // TODO: use stronger hash
        _loginLiveEvent.launchLoadData {
            preferenceStorage.accessToken = authRepository.loginUser(email, cryptor.getMd5Hash(password))
            // TODO: save user in db
        }
    }
}
