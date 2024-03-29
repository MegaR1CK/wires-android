package com.wires.app.presentation.feed.feedchild

import android.animation.AnimatorInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.ItemPostBinding
import com.wires.app.extensions.countViewHeight
import com.wires.app.extensions.enableLinks
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load
import com.wires.app.extensions.showPopupMenu
import com.wires.app.managers.DateFormatter

class PostViewHolder(
    private val itemBinding: ItemPostBinding,
    private val onItemClick: (Int) -> Unit,
    private val onAuthorClick: (Int) -> Unit,
    private val onLikeClick: (Int, Boolean) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val context = itemView.context

    fun bind(post: Post) = with(itemBinding) {
        if (!post.isRemoved) {
            textVewPostTime.text = dateFormatter.getDateTimeRelative(post.publishTime)
            textViewPostBody.text = post.text
            textViewPostBody.enableLinks(root)
            imageViewPostImage.isVisible = post.image != null
            post.image?.let { image ->
                imageViewPostImage.countViewHeight(image.size.width, image.size.height)
                imageViewPostImage.load(image.url)
            } ?: run {
                imageViewPostImage.setImageDrawable(null)
            }
            root.setOnClickListener { onItemClick.invoke(post.id) }
            buttonPostActions.isVisible = post.isEditable
            buttonPostActions.setOnClickListener { button ->
                button.showPopupMenu(R.menu.menu_post_actions) { itemId ->
                    when (itemId) {
                        R.id.postActionEdit -> onEditClick(post.id)
                        R.id.postActionDelete -> onDeleteClick(post.id)
                    }
                }
            }
            bindBottomPanel(post)
            bindPostAuthor(post.author)
        } else {
            // Если пост удален, задаем холдеру нулевую высоту, чтобы скрыть его и не обновлять
            // весь список, так как удаление элемента из пагинируемого списка затруднительно
            root.layoutParams.height = 0
        }
    }

    private fun bindBottomPanel(post: Post) = with(itemBinding) {
        textViewPostLikeCounter.text = post.likesCount.toString()
        textViewPostCommentCounter.text = post.commentsCount.toString()
        imageViewPostLike.isSelected = post.isLiked
        linearLayoutPostLike.setOnClickListener {
            onLikeClick(post.id, !post.isLiked)
            val likeAnimator = AnimatorInflater.loadAnimator(context, R.animator.anim_like_button)
            likeAnimator.setTarget(imageViewPostLike)
            likeAnimator.start()
        }
        linearLayoutPostComment.setOnClickListener {
            onItemClick(post.id)
        }
    }

    private fun bindPostAuthor(author: UserPreview) = with(itemBinding) {
        constraintLayoutPostAuthor.setOnClickListener { onAuthorClick.invoke(author.id) }
        textViewPostAuthor.text = author.getDisplayName()
        imageViewPostAuthorAvatar.load(
            imageUrl = author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
    }
}
