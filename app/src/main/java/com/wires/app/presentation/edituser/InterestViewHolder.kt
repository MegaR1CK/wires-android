package com.wires.app.presentation.edituser

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.data.model.ListInterest
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.ViewTopicChipBinding

class InterestViewHolder(
    private val binding: ViewTopicChipBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(interest: ListInterest, onItemClick: (UserInterest) -> Unit) = with(binding) {
        textViewTab.text = interest.userInterest.value
        root.isSelected = interest.isSelected
        root.setOnClickListener {
            root.isSelected = !root.isSelected
            onItemClick(interest.userInterest)
        }
    }
}
