package com.wires.app.data

import androidx.recyclerview.widget.DiffUtil
import com.wires.app.data.model.Similarable

/**
 * Factory that created DiffUtil.Callback
 * It used in RecyclerView Adapters through Dagger's injections
 */
class DiffUtilCallbackFactory constructor(
    private val diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory
) {

    fun <T : Similarable<T>> create(oldList: List<T>, newList: List<T>): DiffUtil.Callback {

        val diffUtilItemCallback = diffUtilItemCallbackFactory.create<T>()

        return object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                diffUtilItemCallback.areItemsTheSame(
                    oldList[oldItemPosition],
                    newList[newItemPosition]
                )

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                diffUtilItemCallback.areContentsTheSame(
                    oldList[oldItemPosition],
                    newList[newItemPosition]
                )
        }
    }
}
