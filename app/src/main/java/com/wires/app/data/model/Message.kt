package com.wires.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Message(
    val id: Int,
    val author: UserPreview,
    val text: String,
    val sendTime: LocalDateTime,
    val isInitial: Boolean,
    val isRead: Boolean
) : Parcelable
