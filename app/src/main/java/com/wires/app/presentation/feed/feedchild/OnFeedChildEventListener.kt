package com.wires.app.presentation.feed.feedchild

import com.wires.app.data.LoadableResult

interface OnFeedChildEventListener {
    fun onLoadingStateChanged(state: LoadableResult<*>)
    fun onOpenPostUpdate(postId: Int)
}
