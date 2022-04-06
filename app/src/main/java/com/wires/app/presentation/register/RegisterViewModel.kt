package com.wires.app.presentation.register

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

class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val validator: Validator,
    private val cryptor: Cryptor
) : BaseViewModel() {

    private val _usernameErrorLiveData = MutableLiveData<Int?>()
    val usernameErrorLiveData: LiveData<Int?> = _usernameErrorLiveData

    private val _emailErrorLiveData = MutableLiveData<Int?>()
    val emailErrorLiveData: LiveData<Int?> = _emailErrorLiveData

    private val _passwordErrorLiveData = MutableLiveData<Int?>()
    val passwordErrorLiveData: LiveData<Int?> = _passwordErrorLiveData

    private val _confirmPasswordErrorLiveData = MutableLiveData<Int?>()
    val confirmPasswordErrorLiveData: LiveData<Int?> = _confirmPasswordErrorLiveData

    private val _registerLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val registerLiveEvent: LiveData<LoadableResult<Unit>> = _registerLiveEvent

    fun validateEmail(email: String) {
        _emailErrorLiveData.value = validator.validateEmail(email)
    }

    fun validatePassword(password: String) {
        _passwordErrorLiveData.value = validator.validatePassword(password)
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
        _confirmPasswordErrorLiveData.value = validator.validateConfirmPasswords(password, confirmPassword)
    }

    fun validateUsername(username: String) {
        _usernameErrorLiveData.value = validator.validateUsername(username)
    }

    fun registerUser(email: String, password: String, confirmPassword: String, username: String) {
        validateUsername(username)
        validateEmail(email)
        validatePassword(password)
        validateConfirmPassword(password, confirmPassword)
        if (_usernameErrorLiveData.value != null ||
            _emailErrorLiveData.value != null ||
            _passwordErrorLiveData.value != null ||
            _confirmPasswordErrorLiveData.value != null
        ) return
        val passwordHash = cryptor.getSha256Hash(password, email)
        _registerLiveEvent.launchLoadData {
            authRepository.registerUser(username, email, passwordHash)
            tokenRepository.setAccessToken(authRepository.loginUser(email, passwordHash).token)
            userRepository.storeUser(userRepository.getCurrentUser())
        }
    }
}
