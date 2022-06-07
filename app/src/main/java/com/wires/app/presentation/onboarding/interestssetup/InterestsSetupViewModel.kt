package com.wires.app.presentation.onboarding.interestssetup

import androidx.lifecycle.LiveData
import com.wires.app.data.model.UserInterest
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class InterestsSetupViewModel @Inject constructor() : BaseViewModel() {

    var selectedInterests = mutableListOf<UserInterest>()

    private val _interestsSubmitLiveEvent = SingleLiveEvent<List<UserInterest>>()
    val interestsSubmitLiveEvent: LiveData<List<UserInterest>> = _interestsSubmitLiveEvent

    fun submitInterests() {
        _interestsSubmitLiveEvent.postValue(selectedInterests)
    }
}
