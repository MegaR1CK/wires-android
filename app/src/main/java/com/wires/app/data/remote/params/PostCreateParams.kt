package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class PostCreateParams(
    @SerializedName("text")
    val text: String,
    @SerializedName("topic")
    val topic: String
)
