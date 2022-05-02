package com.wires.app.presentation.pickusers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.ItemUserBinding
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class FoundUsersAdapter @Inject constructor() : BaseAdapter<UserPreview, FoundUserViewHolder>() {

    var onItemClick: (UserPreview) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundUserViewHolder {
        return FoundUserViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FoundUserViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    fun updateSelectedItems(selectedItems: List<UserPreview>) = items.forEach { item ->
        item.isSelected = selectedItems.map { it.id }.contains(item.id)
        notifyItemChanged(items.indexOf(item))
    }
}
