package com.wires.app.presentation.createpost

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.ItemTopicBinding
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class TopicsAdapter @Inject constructor() : BaseAdapter<UserInterest, TopicViewHolder>() {

    var onTopicSelect: (UserInterest) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(items[position], onTopicSelect)
    }
}
