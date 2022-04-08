package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CommentResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    val author: UserPreviewResponse,
    @SerializedName("text")
    val text: String,
    @SerializedName("send_time")
    val sendTime: LocalDateTime
)
