package com.wires.app.presentation.feed.feedchild

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Post
import com.wires.app.databinding.ItemPostBinding
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class PostViewHolder(
    private val itemBinding: ItemPostBinding,
    private val onItemClick: (Int) -> Unit,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(post: Post) = with(itemBinding) {
        val context = itemView.context
        textViewPostAuthor.text = context.getString(
            R.string.feed_post_author_name,
            post.author.firstName,
            post.author.lastName
        )
        textVewPostTime.text = dateFormatter.dateTimeToStringRelative(post.publishTime)
        imageViewPostAuthorAvatar.load(
            post.author.avatarUrl,
            isCircle = true
        )
        textViewPostBody.text = post.text
        imageViewPostImage.load(post.imageUrl)
        textViewPostLikeCounter.text = post.likesCount.toString()
        textViewPostCommentCounter.text = post.commentsCount.toString()
        root.setOnClickListener { onItemClick.invoke(post.id) }
    }
}