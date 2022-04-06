package com.wires.app.presentation.post

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Comment
import com.wires.app.databinding.ItemCommentBinding
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment) = with(binding) {
        imageViewCommentAuthorAvatar.load(comment.author.avatarUrl, isCircle = true)
        if (!comment.author.firstName.isNullOrEmpty() && !comment.author.lastName.isNullOrEmpty()) {
            textViewCommentAuthorName.text = itemView.context.getString(
                R.string.feed_post_author_name,
                comment.author.firstName,
                comment.author.lastName
            )
        } else {
            textViewCommentAuthorName.text = comment.author.username
        }
        textViewCommentBody.text = comment.text
        textViewCommentTime.text = dateFormatter.dateTimeToStringRelative(comment.publishTime)
    }
}
