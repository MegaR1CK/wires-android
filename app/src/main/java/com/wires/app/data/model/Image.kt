package com.wires.app.data.model

import java.io.Serializable

data class Image(
    val url: String,
    val size: ImageSize
) : Serializable
