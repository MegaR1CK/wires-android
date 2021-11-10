package com.wires.app.data.model

import android.graphics.Bitmap

data class CreatedPost(
    override val author: User,
    override val text: String,
    val imageBitmap: Bitmap?
) : BasePost(author, text)
