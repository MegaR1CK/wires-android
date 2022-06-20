package com.wires.app.presentation.post

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Comment
import com.wires.app.databinding.ItemCommentBinding
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment, onItemClick: (Int) -> Unit) = with(binding) {
        imageViewCommentAuthorAvatar.load(
            imageUrl = comment.author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewCommentAuthorName.text = comment.author.getDisplayName()
        textViewCommentBody.text = comment.text
        textViewCommentTime.text = dateFormatter.getDateTimeRelative(comment.sendTime)
        root.setOnClickListener {
            onItemClick(comment.author.id)
        }
    }
}
