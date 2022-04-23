package com.wires.app.presentation.edituser

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.ListInterest
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.ViewTopicChipBinding
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class InterestsAdapter @Inject constructor() : BaseAdapter<ListInterest, InterestViewHolder>() {

    var onItemClick: (UserInterest) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        return InterestViewHolder(ViewTopicChipBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }
}
