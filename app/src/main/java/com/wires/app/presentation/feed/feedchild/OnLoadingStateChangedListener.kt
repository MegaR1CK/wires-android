package com.wires.app.presentation.feed.feedchild

import com.wires.app.data.LoadableResult

interface OnLoadingStateChangedListener {
    fun onLoadingStateChanged(state: LoadableResult<*>)
}
