package com.wires.app.presentation.pickusers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.ItemUserBinding
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class UsersAdapter @Inject constructor() : BaseAdapter<UserPreview, UserViewHolder>() {

    var onItemClick: (UserPreview) -> Unit = { }
    var checkboxesEnabled = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position], checkboxesEnabled, onItemClick)
    }

    fun updateSelectedItems(selectedItems: List<UserPreview>) = items.forEach { item ->
        item.isSelected = selectedItems.map { it.id }.contains(item.id)
        notifyItemChanged(items.indexOf(item))
    }
}
