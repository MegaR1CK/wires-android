package com.wires.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val url: String,
    val size: ImageSize
) : Parcelable
