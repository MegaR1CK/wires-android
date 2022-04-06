package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ObjectResponse<T>(
    @SerializedName("data")
    val data: T
)
