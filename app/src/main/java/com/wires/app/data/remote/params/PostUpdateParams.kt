package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class PostUpdateParams(
    @SerializedName("text")
    val text: String?,
    @SerializedName("topic")
    val topic: String?
)
