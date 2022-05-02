package com.wires.app.presentation.pickusers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.ItemUserPreviewBinding
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class AddedUsersAdapter @Inject constructor() : BaseAdapter<UserPreview, AddedUserViewHolder>() {

    var onCancelClick: (UserPreview) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedUserViewHolder {
        return AddedUserViewHolder(
            ItemUserPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddedUserViewHolder, position: Int) {
        holder.bind(items[position], onCancelClick)
    }
}
