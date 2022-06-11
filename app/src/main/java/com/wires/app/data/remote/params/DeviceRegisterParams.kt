package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class DeviceRegisterParams(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("push_token")
    val pushToken: String
)
