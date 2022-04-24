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
import javax.inject.Inject

class EditUserViewModel @Inject constructor(
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    var selectedInterests = mutableListOf<UserInterest>()

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _updateUserLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val updateUserLiveEvent: LiveData<LoadableResult<Unit>> = _updateUserLiveEvent

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun updateUser(
        username: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        avatarPath: String?
    ) {
        _updateUserLiveEvent.launchLoadData(
            updateUserUseCase
                .executeLoadable(UpdateUserUseCase.Params(username, email, firstName, lastName, selectedInterests, avatarPath))
        )
    }
}
