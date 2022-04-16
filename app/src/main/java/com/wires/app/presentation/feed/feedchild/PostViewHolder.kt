package com.wires.app.presentation.feed.feedchild

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Post
import com.wires.app.databinding.ItemPostBinding
import com.wires.app.extensions.countViewHeight
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class PostViewHolder(
    private val itemBinding: ItemPostBinding,
    private val onItemClick: (Int) -> Unit,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(post: Post) = with(itemBinding) {
        val context = itemView.context
        if (!post.author.firstName.isNullOrEmpty() && !post.author.lastName.isNullOrEmpty()) {
            textViewPostAuthor.text = context.getString(
                R.string.feed_post_author_name,
                post.author.firstName,
                post.author.lastName
            )
        } else {
            textViewPostAuthor.text = post.author.username
        }

        textVewPostTime.text = dateFormatter.dateTimeToStringRelative(post.publishTime)
        imageViewPostAuthorAvatar.load(
            imageUrl = post.author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewPostBody.text = post.text
        post.image?.let { image ->
            imageViewPostImage.countViewHeight(image.size.width, image.size.height)
            imageViewPostImage.load(image.url)
        }
        textViewPostLikeCounter.text = post.likesCount.toString()
        textViewPostCommentCounter.text = post.commentsCount.toString()
        root.setOnClickListener { onItemClick.invoke(post.id) }
    }
}
