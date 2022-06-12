package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class UserLogoutParams(
    @SerializedName("device_id")
    val deviceId: String
)
