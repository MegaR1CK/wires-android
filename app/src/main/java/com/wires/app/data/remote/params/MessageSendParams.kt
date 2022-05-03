package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class MessageSendParams(
    @SerializedName("text")
    val text: String,
    @SerializedName("is_initial")
    val isInitial: Boolean
)
