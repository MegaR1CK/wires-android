package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

open class ObjectResponse<out T>(
    @SerializedName("data")
    val data: T
)
