package com.wires.app.domain.repository

import com.wires.app.data.preferences.PreferenceStorage
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) {
    val isSignedIn = !preferenceStorage.accessToken.isNullOrEmpty()
}
