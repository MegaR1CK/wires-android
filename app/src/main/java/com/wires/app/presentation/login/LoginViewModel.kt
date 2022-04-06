package com.wires.app.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.Cryptor
import com.wires.app.domain.repository.AuthRepository
import com.wires.app.domain.repository.TokenRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import com.wires.app.presentation.utils.Validator
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val validator: Validator,
    private val cryptor: Cryptor
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
        _loginLiveEvent.launchLoadData {
            tokenRepository.setAccessToken(
                authRepository.loginUser(email, cryptor.getSha256Hash(password, email)).token
            )
            userRepository.storeUser(userRepository.getCurrentUser())
        }
    }
}
