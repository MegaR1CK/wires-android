package com.wires.app.data.model

import android.graphics.Bitmap

data class CreatedPost(
    override val author: UserPreview,
    override val text: String,
    val imageBitmap: Bitmap?
) : BasePost(author, text)
