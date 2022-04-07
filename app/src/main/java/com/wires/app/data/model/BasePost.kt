package com.wires.app.data.model

abstract class BasePost(
    open val author: UserPreview,
    open val text: String
)
