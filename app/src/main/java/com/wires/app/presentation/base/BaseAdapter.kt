package com.wires.app.presentation.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected val items = mutableListOf<T>()

    override fun getItemCount() = items.size

    val isEmpty: Boolean
        get() = itemCount == 0

    fun getItem(position: Int): T {
        return items[position]
    }

    open fun submitList(items: List<T>) {
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    open fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }
}
