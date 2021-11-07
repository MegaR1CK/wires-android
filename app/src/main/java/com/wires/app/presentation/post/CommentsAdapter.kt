package com.wires.app.presentation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.Comment
import com.wires.app.databinding.ItemCommentBinding
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class CommentsAdapter @Inject constructor(
    private val dateFormatter: DateFormatter
) : BaseAdapter<Comment, CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            dateFormatter
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
