package com.wires.app.presentation.onboarding.interestssetup

import android.content.Context
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.ListInterest
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentInterestsSetupBinding
import com.wires.app.extensions.addFlexboxSpaceItemDecoration
import com.wires.app.extensions.addOrRemove
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.edituser.InterestsAdapter
import com.wires.app.presentation.onboarding.OnSubmitListener
import javax.inject.Inject

class InterestsSetupFragment : BaseFragment(R.layout.fragment_interests_setup) {

    private val binding by viewBinding(FragmentInterestsSetupBinding::bind)
    private val viewModel: InterestsSetupViewModel by appViewModels()

    private var onSubmitListener: OnSubmitListener? = null

    @Inject lateinit var interestsAdapter: InterestsAdapter

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        buttonOnboardingConfirm.setOnClickListener {
            viewModel.submitInterests()
        }
        setupInterestsList()
    }

    override fun onBindViewModel() = with(viewModel) {
        interestsSubmitLiveEvent.observe { interests ->
            onSubmitListener?.onSubmitInterests(interests)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onSubmitListener = parentFragment as? OnSubmitListener
    }

    fun <T> setButtonState(result: LoadableResult<T>) {
        binding.buttonOnboardingConfirm.isLoading = result.isLoading
    }

    private fun setupInterestsList() = with(binding.recyclerViewOnboardingInterests) {
        addFlexboxSpaceItemDecoration(R.dimen.interests_list_spacing)
        layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        adapter = interestsAdapter.apply {
            submitList(UserInterest.values().map { ListInterest(it, isSelected = false) })
            onItemClick = { interest -> viewModel.selectedInterests.addOrRemove(interest) }
        }
    }
}
