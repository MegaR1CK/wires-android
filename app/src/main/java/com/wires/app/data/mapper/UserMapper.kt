package com.wires.app.data.mapper

import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import com.wires.app.data.remote.response.UserResponse
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun fromResponseToModel(userResponse: UserResponse): User {
        return User(
            id = userResponse.id,
            email = userResponse.email,
            username = userResponse.username,
            firstName = null,
            lastName = null,
            avatarUrl = userResponse.avatarUrl,
            interests = userResponse.interests.map { UserInterest.valueOf(it) }
        )
    }
}
