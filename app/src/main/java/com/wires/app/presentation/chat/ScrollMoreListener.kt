package com.wires.app.presentation.chat

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Скролл листенер для ресайклера, реализующий ручную пагинацию
 */
class ScrollMoreListener(
    private val layoutManager: LinearLayoutManager,
    private val loadMore: (Int) -> Unit
) : RecyclerView.OnScrollListener() {

    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) loading = true
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }
        val visibleThreshold = 5
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            loadMore(totalItemCount)
            loading = true
        }
    }
}
