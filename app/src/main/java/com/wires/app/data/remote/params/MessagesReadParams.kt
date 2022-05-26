package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class MessagesReadParams(
    @SerializedName("messages_ids")
    val messagesIds: Set<Int>
)
