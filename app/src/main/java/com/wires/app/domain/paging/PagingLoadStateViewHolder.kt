package com.wires.app.domain.paging

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.ItemPagingStateBinding
import com.wires.app.extensions.inflate
import com.wires.app.extensions.parseError

/**
 * Отображает ошибку и прогресс в ячейке при пагинации
 */
class PagingLoadStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    parent.inflate(R.layout.item_paging_state)
) {
    private val binding by viewBinding(ItemPagingStateBinding::bind)

    init {
        binding.buttonPagingRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) = with(binding) {
        if (loadState is LoadState.Error) {
            val parsedError = loadState.error.parseError()
            textViewPagingErrorTitle.text = parsedError.title
            textViewPagingErrorDescription.text = parsedError.message
        }
        progressBarPagingLoading.isVisible = loadState is LoadState.Loading
        buttonPagingRetry.isVisible = loadState !is LoadState.Loading
        textViewPagingErrorTitle.isVisible = loadState !is LoadState.Loading
        textViewPagingErrorDescription.isVisible = loadState !is LoadState.Loading
    }
}
