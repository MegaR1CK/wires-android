package com.wires.app.presentation.views

import com.wires.app.data.LoadableResult

interface ResultAwareView {
    fun handleResult(result: LoadableResult<*>)
}
