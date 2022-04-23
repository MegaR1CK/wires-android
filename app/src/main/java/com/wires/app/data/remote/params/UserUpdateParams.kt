package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName
import com.wires.app.data.model.UserInterest

data class UserUpdateParams(
    @SerializedName("username")
    val username: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("interests")
    val interests: List<UserInterest>
)
