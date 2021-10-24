package com.wires.app.data

import androidx.recyclerview.widget.DiffUtil
import com.wires.app.data.model.Similarable

/**
 * Factory that created DiffUtil.ItemCallback
 * It used in RecyclerView Adapters through Dagger's injections
 */
class DiffUtilItemCallbackFactory {

    fun <T : Similarable<T>> create(): DiffUtil.ItemCallback<T> {
        return object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.areItemsTheSame(newItem)
            override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.areContentsTheSame(newItem)
        }
    }
}
