package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class CommentAddParams(
    @SerializedName("text")
    val text: String
)
