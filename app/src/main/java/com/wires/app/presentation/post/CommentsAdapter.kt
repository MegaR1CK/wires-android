package com.wires.app.presentation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.wires.app.data.DiffUtilItemCallbackFactory
import com.wires.app.data.model.Comment
import com.wires.app.databinding.ItemCommentBinding
import com.wires.app.managers.DateFormatter
import javax.inject.Inject

class CommentsAdapter @Inject constructor(
    diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory,
    private val dateFormatter: DateFormatter
) : PagingDataAdapter<Comment, CommentViewHolder>(diffUtilItemCallbackFactory.create()) {

    var onItemClick: (Int) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            dateFormatter
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onItemClick) }
    }
}
