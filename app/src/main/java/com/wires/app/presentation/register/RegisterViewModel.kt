package com.wires.app.presentation.register

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.domain.usecase.auth.RegisterUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel() {

    private val _registerLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val registerLiveEvent: LiveData<LoadableResult<Unit>> = _registerLiveEvent

    fun registerUser(username: String, email: String, password: String) {
        _registerLiveEvent.launchLoadData(registerUseCase.executeLoadable(RegisterUseCase.Params(username, email, password)))
    }
}
