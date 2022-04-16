package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PostResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    val author: UserPreviewResponse,
    @SerializedName("text")
    val text: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("image")
    val image: ImageResponse?,
    @SerializedName("publish_time")
    val publishTime: LocalDateTime,
    @SerializedName("likes_count")
    val likesCount: Int,
    @SerializedName("comments_count")
    val commentsCount: Int,
    @SerializedName("is_user_liked")
    val isUserLiked: Boolean
)
