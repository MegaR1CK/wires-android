package com.wires.app.presentation.edituser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserInterest
import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.domain.usecase.user.UpdateUserUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import com.wires.app.presentation.utils.Validator
import javax.inject.Inject

class EditUserViewModel @Inject constructor(
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val validator: Validator
) : BaseViewModel() {

    var selectedInterests = mutableListOf<UserInterest>()

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _updateUserLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val updateUserLiveEvent: LiveData<LoadableResult<Unit>> = _updateUserLiveEvent

    private val _emailErrorLiveData = MutableLiveData<Int?>()
    val emailErrorLiveData: LiveData<Int?> = _emailErrorLiveData

    private val _usernameErrorLiveData = MutableLiveData<Int?>()
    val usernameErrorLiveData: LiveData<Int?> = _usernameErrorLiveData

    private val _firstNameErrorLiveData = MutableLiveData<Int?>()
    val firstNameErrorLiveData: LiveData<Int?> = _firstNameErrorLiveData

    private val _lastNameErrorLiveData = MutableLiveData<Int?>()
    val lastNameErrorLiveData: LiveData<Int?> = _lastNameErrorLiveData

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun validateEmail(email: String?) = email?.let {
        _emailErrorLiveData.value = validator.validateEmail(it)
    }

    fun validateUsername(username: String?) = username?.let {
        _usernameErrorLiveData.value = validator.validateUsername(it)
    }

    fun validateFirstName(firstName: String?) = firstName?.let {
        _firstNameErrorLiveData.value = validator.validateName(it)
    }

    fun validateLastName(lastName: String?) = lastName?.let {
        _lastNameErrorLiveData.value = validator.validateName(it)
    }

    fun updateUser(
        username: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        avatarPath: String?
    ) {
        validateEmail(email)
        validateUsername(username)
        validateFirstName(firstName)
        validateLastName(lastName)
        if (_emailErrorLiveData.value != null ||
            _usernameErrorLiveData.value != null ||
            _firstNameErrorLiveData.value != null ||
            _lastNameErrorLiveData.value != null
        ) return
        _updateUserLiveEvent.launchLoadData(
            updateUserUseCase
                .executeLoadable(UpdateUserUseCase.Params(username, email, firstName, lastName, selectedInterests, avatarPath))
        )
    }
}
