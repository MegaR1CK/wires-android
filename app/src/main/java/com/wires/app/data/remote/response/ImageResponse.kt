package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("url")
    val url: String,
    @SerializedName("size")
    val size: ImageSizeResponse
)
