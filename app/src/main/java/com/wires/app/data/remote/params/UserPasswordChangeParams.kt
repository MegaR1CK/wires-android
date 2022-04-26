package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class UserPasswordChangeParams(
    @SerializedName("old_password_hash")
    val oldPasswordHash: String,
    @SerializedName("new_password_hash")
    val newPasswordHash: String
)
