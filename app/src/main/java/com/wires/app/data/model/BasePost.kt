package com.wires.app.data.model

abstract class BasePost(
    open val author: User,
    open val text: String
)
