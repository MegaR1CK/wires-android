package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class MessageResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    val author: UserPreviewResponse,
    @SerializedName("text")
    val text: String,
    @SerializedName("send_time")
    val sendTime: LocalDateTime,
    @SerializedName("is_initial")
    val isInitial: Boolean,
    @SerializedName("is_read")
    val isRead: Boolean
)
