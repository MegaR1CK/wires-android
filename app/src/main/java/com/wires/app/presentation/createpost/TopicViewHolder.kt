package com.wires.app.presentation.createpost

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.ItemTopicBinding

class TopicViewHolder(
    private val itemBinding: ItemTopicBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(interest: UserInterest, onTopicSelect: (UserInterest) -> Unit) = with(itemBinding.root) {
        text = interest.value
        setOnClickListener {
            onTopicSelect(interest)
        }
    }
}
