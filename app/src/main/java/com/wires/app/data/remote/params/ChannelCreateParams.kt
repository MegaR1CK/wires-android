package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class ChannelCreateParams(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("members_ids")
    val membersIds: List<Int>
)
