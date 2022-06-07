package com.wires.app.presentation.onboarding

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.usecase.user.UpdateUserUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    var userData: UserSetupData? = null
    var interests: List<UserInterest> = emptyList()

    private val _updateUserLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val updateUserLiveEvent: LiveData<LoadableResult<Unit>> = _updateUserLiveEvent

    private val _skipOnboardingLiveEvent = SingleLiveEvent<Unit>()
    val skipOnboardingLiveEvent: LiveData<Unit> = _skipOnboardingLiveEvent

    fun setUserData() {
        _updateUserLiveEvent.launchLoadData(
            updateUserUseCase.executeLoadable(
                UpdateUserUseCase.Params(
                    firstName = userData?.firstName,
                    lastName = userData?.lastName,
                    interests = interests
                )
            )
        )
    }

    fun skipOnboarding() = _skipOnboardingLiveEvent.postValue(Unit)
}
