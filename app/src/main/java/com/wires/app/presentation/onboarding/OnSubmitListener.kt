package com.wires.app.presentation.onboarding

import com.wires.app.data.model.UserInterest

interface OnSubmitListener {

    fun onSubmitUserData(userSetupData: UserSetupData)

    fun onSubmitInterests(interests: List<UserInterest>)
}
