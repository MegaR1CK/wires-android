package com.wires.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class EmptyRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    var emptyView: View? = null
        set(view) {
            field = view
            checkIfEmpty()
        }

    private val observer = object : AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    fun checkIfEmpty() {
        val emptyView = this.emptyView
        val adapter = this.adapter
        if (emptyView != null && adapter != null) {
            val emptyViewVisible = adapter.itemCount == 0
            if (emptyViewVisible) {
                isVisible = false
                emptyView.isVisible = true
            } else {
                isVisible = true
                emptyView.isVisible = false
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)
        checkIfEmpty()
    }
}
