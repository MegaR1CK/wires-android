package com.wires.app.presentation.feed.feedchild

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.Post
import com.wires.app.databinding.ItemPostBinding
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class PostsAdapter @Inject constructor(
    private val dateFormatter: DateFormatter
) : BaseAdapter<Post, PostViewHolder>() {

    var onPostClick: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false), onPostClick, dateFormatter
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
