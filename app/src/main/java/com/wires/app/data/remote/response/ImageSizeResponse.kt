package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ImageSizeResponse(
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)
