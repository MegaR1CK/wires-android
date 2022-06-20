package com.wires.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageSize(
    val width: Int,
    val height: Int
) : Parcelable
