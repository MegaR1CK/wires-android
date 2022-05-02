package com.wires.app.presentation.pickusers

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.ItemUserPreviewBinding
import com.wires.app.extensions.load

class AddedUserViewHolder(
    private val itemBinding: ItemUserPreviewBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(userPreview: UserPreview, onCancelClick: (UserPreview) -> Unit) = with(itemBinding) {
        imageViewPreviewAvatar.load(
            imageUrl = userPreview.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewPreviewName.text = userPreview.firstName ?: userPreview.username
        buttonPreviewCancel.setOnClickListener {
            onCancelClick(userPreview)
        }
    }
}
