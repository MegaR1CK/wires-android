package com.wires.app.presentation.onboarding.usersetup

import androidx.lifecycle.LiveData
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import com.wires.app.presentation.onboarding.UserSetupData
import javax.inject.Inject

class UserSetupViewModel @Inject constructor() : BaseViewModel() {

    var selectedAvatarPath: String? = null

    private val _submitDataLiveEvent = SingleLiveEvent<UserSetupData>()
    val submitDataLiveEvent: LiveData<UserSetupData> = _submitDataLiveEvent

    fun submitData(firstName: String, lastName: String) {
        _submitDataLiveEvent.postValue(UserSetupData(firstName, lastName, selectedAvatarPath))
    }
}
