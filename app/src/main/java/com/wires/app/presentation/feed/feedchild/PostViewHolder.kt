package com.wires.app.presentation.feed.feedchild

import android.animation.AnimatorInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Post
import com.wires.app.databinding.ItemPostBinding
import com.wires.app.extensions.countViewHeight
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class PostViewHolder(
    private val itemBinding: ItemPostBinding,
    private val onItemClick: (Int) -> Unit,
    private val onAuthorClick: (Int) -> Unit,
    private val onLikeClick: (Int, Boolean) -> Unit,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(post: Post) = with(itemBinding) {
        val context = itemView.context
        textViewPostAuthor.text = post.author.getDisplayName()
        textVewPostTime.text = dateFormatter.dateTimeToStringRelative(post.publishTime)
        imageViewPostAuthorAvatar.load(
            imageUrl = post.author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewPostBody.text = post.text
        imageViewPostImage.isVisible = post.image != null
        post.image?.let { image ->
            imageViewPostImage.countViewHeight(image.size.width, image.size.height)
            imageViewPostImage.load(image.url)
        } ?: run {
            imageViewPostImage.setImageDrawable(null)
        }
        textViewPostLikeCounter.text = post.likesCount.toString()
        textViewPostCommentCounter.text = post.commentsCount.toString()
        imageViewPostLike.isSelected = post.isLiked
        root.setOnClickListener { onItemClick.invoke(post.id) }
        constraintLayoutPostAuthor.setOnClickListener { onAuthorClick.invoke(post.author.id) }
        linearLayoutPostLike.setOnClickListener {
            onLikeClick(post.id, !post.isLiked)
            val likeAnimator = AnimatorInflater.loadAnimator(context, R.animator.anim_like_button)
            likeAnimator.setTarget(imageViewPostLike)
            likeAnimator.start()
        }
        linearLayoutPostComment.setOnClickListener {
            onItemClick(post.id)
        }
        linearLayoutPostShare.setOnClickListener { }
    }
}
