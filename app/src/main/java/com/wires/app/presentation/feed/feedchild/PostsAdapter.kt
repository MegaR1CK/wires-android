package com.wires.app.presentation.feed.feedchild

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.wires.app.data.DiffUtilItemCallbackFactory
import com.wires.app.data.model.Post
import com.wires.app.databinding.ItemPostBinding
import com.wires.app.managers.DateFormatter
import javax.inject.Inject

class PostsAdapter @Inject constructor(
    diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory,
    private val dateFormatter: DateFormatter
) : PagingDataAdapter<Post, PostViewHolder>(diffUtilItemCallbackFactory.create()) {

    var onPostClick: (Int) -> Unit = {}
    var onAuthorClick: (Int) -> Unit = {}
    var onLikeClick: (Int, Boolean) -> Unit = { _, _ -> }
    var onEditClick: (Int) -> Unit = {}
    var onDeleteClick: (Int) -> Unit = {}

    val isEmpty: Boolean
        get() = snapshot().none { it?.isRemoved == false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPostClick,
            onAuthorClick,
            onLikeClick,
            onEditClick,
            onDeleteClick,
            dateFormatter
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun updatePostLike(postId: Int) {
        updatePostById(postId) { post ->
            post.isLiked = !post.isLiked
            post.likesCount = if (post.isLiked) post.likesCount.inc() else post.likesCount.dec()
        }
    }

    fun updatePostComments(postId: Int, commentsCount: Int) {
        updatePostById(postId) { post ->
            post.commentsCount = commentsCount
        }
    }

    fun updatePostData(updatedPost: Post) {
        updatePostById(updatedPost.id) { post ->
            post.text = updatedPost.text
            post.topic = updatedPost.topic
            post.image = updatedPost.image
        }
    }

    fun removePost(postId: Int) {
        updatePostById(postId) { post ->
            post.isRemoved = true
        }
    }

    private fun updatePostById(postId: Int, action: (Post) -> Unit) {
        snapshot().find { it?.id == postId }?.let { post ->
            action(post)
            notifyItemChanged(snapshot().indexOf(post))
        }
    }
}
