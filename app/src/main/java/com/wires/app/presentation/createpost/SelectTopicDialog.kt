package com.wires.app.presentation.createpost

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.DialogTopicSelectBinding
import com.wires.app.extensions.navigateBack

class SelectTopicDialog : DialogFragment(R.layout.dialog_topic_select) {

    companion object {
        const val SELECT_INTEREST_RESULT_KEY = "select_interest_result"
        const val SELECTED_INTEREST_KEY = "selected_interest"
    }

    private val binding by viewBinding(DialogTopicSelectBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewTopicSelect.adapter = TopicsAdapter().apply {
            submitList(UserInterest.values().toList())
            onTopicSelect = { interest ->
                setFragmentResult(SELECT_INTEREST_RESULT_KEY, bundleOf(SELECTED_INTEREST_KEY to interest))
                navigateBack()
            }
        }
    }
}
