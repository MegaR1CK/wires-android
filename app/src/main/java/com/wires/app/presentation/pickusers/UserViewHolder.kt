package com.wires.app.presentation.pickusers

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.ItemUserBinding
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load

class UserViewHolder(
    private val itemBinding: ItemUserBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(userPreview: UserPreview, checkboxesEnabled: Boolean, onItemClick: (UserPreview) -> Unit) = with(itemBinding) {
        imageViewUserAvatar.load(
            imageUrl = userPreview.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewUserName.text = userPreview.getDisplayName()
        checkboxUser.isVisible = checkboxesEnabled
        checkboxUser.isChecked = userPreview.isSelected
        checkboxUser.setOnClickListener {
            onItemClick(userPreview)
        }
        root.setOnClickListener {
            checkboxUser.isChecked = !checkboxUser.isChecked
            onItemClick(userPreview)
        }
    }
}
