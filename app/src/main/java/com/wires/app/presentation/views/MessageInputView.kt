package com.wires.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.databinding.ViewMessageInputBinding

class MessageInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ResultAwareView {

    private val binding = ViewMessageInputBinding.inflate(LayoutInflater.from(context), this, true)

    val isLoadingState: Boolean
        get() = binding.progressSend.isVisible

    override fun handleResult(result: LoadableResult<*>) = with(binding) {
        buttonSend.isInvisible = result.isLoading
        progressSend.isInvisible = !result.isLoading
        result.doOnSuccess { editTextMessageInput.text = null }
    }

    init {
        binding.editTextMessageInput.doOnTextChanged { text, _, _, _ ->
            binding.buttonSend.isInvisible = text.isNullOrBlank()
        }
        if (attrs != null) {
            context.withStyledAttributes(
                attrs,
                R.styleable.MessageInputView,
                defStyleAttr
            ) {
                binding.editTextMessageInput.hint = getString(R.styleable.MessageInputView_inputHint)
            }
        }
    }

    fun setOnSendClickListener(block: (String) -> Unit) {
        binding.buttonSend.setOnClickListener { block(binding.editTextMessageInput.text.toString().trim()) }
    }
}
