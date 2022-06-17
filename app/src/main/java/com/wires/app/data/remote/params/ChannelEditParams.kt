package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class ChannelEditParams(
    @SerializedName("name")
    val name: String,
    @SerializedName("members_ids")
    val membersIds: List<Int>
)
