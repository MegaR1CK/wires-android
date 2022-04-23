package com.wires.app.presentation.edituser

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wires.app.R
import com.wires.app.data.model.ListInterest
import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentEditUserBinding
import com.wires.app.extensions.addFlexboxSpaceItemDecoration
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.getInputTextOrNull
import com.wires.app.extensions.load
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class EditUserFragment : BaseFragment(R.layout.fragment_edit_user) {

    companion object {
        const val USER_UPDATED_RESULT_KEY = "user_updated"
    }

    private val binding by viewBinding(FragmentEditUserBinding::bind)
    private val viewModel: EditUserViewModel by appViewModels()

    private var emailChanged = false
    private var usernameChanged = false
    private var firstNameChanged = false
    private var lastNameChanged = false

    @Inject lateinit var interestsAdapter: InterestsAdapter

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        toolbarEditProfile.setNavigationOnClickListener { findNavController().popBackStack() }
        buttonEditProfileDone.setOnClickListener {
            viewModel.updateUser(
                username = editTextEditProfileUsername.getInputTextOrNull(),
                email = editTextEditProfileEmail.getInputTextOrNull(),
                firstName = editTextEditProfileFirstName.getInputTextOrNull(),
                lastName = editTextEditProfileLastName.getInputTextOrNull(),
                avatarPath = null
            )
        }
        editTextEditProfileEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateEmail(editTextEditProfileEmail.getInputText())
        }
        editTextEditProfileUsername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateUsername(editTextEditProfileUsername.getInputText())
        }
        editTextEditProfileFirstName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateFirstName(editTextEditProfileFirstName.getInputText())
        }
        editTextEditProfileLastName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateLastName(editTextEditProfileLastName.getInputText())
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        userLiveData.observe { result ->
            binding.stateViewFlipperEditProfile.setStateFromResult(result)
            result.doOnSuccess { wrapper ->
                wrapper.user?.let(::setupUser)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        updateUserLiveEvent.observe { result ->
            binding.progressIndicatorEditProfile.isVisible = result.isLoading
            result.doOnSuccess {
                setFragmentResult(USER_UPDATED_RESULT_KEY, bundleOf())
                findNavController().popBackStack()
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
        usernameErrorLiveData.observe { errorRes ->
            binding.inputLayoutEditProfileUsername.error = errorRes?.let { getString(it) }
        }
        emailErrorLiveData.observe { errorRes ->
            binding.inputLayoutEditProfileEmail.error = errorRes?.let { getString(it) }
        }
        firstNameErrorLiveData.observe { errorRes ->
            binding.inputLayoutEditProfileFirstName.error = errorRes?.let { getString(it) }
        }
        lastNameErrorLiveData.observe { errorRes ->
            binding.inputLayoutEditProfileLastName.error = errorRes?.let { getString(it) }
        }
    }

    private fun setupUser(user: User) = with(binding) {
        imageViewEditProfileAvatar.load(
            imageUrl = user.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        editTextEditProfileFirstName.setText(user.firstName)
        editTextEditProfileLastName.setText(user.lastName)
        editTextEditProfileEmail.setText(user.email)
        editTextEditProfileUsername.setText(user.username)
        viewModel.selectedInterests.addAll(user.interests)
        setupInterests(user.interests)
        editTextEditProfileEmail.doOnTextChanged { text, _, _, _ ->
            emailChanged = text.toString() != user.email
            setDoneButtonVisibility(user.interests)
        }
        editTextEditProfileUsername.doOnTextChanged { text, _, _, _ ->
            usernameChanged = text.toString() != user.username
            setDoneButtonVisibility(user.interests)
        }
        editTextEditProfileFirstName.doOnTextChanged { text, _, _, _ ->
            firstNameChanged = text.toString() != user.firstName.orEmpty()
            setDoneButtonVisibility(user.interests)
        }
        editTextEditProfileLastName.doOnTextChanged { text, _, _, _ ->
            lastNameChanged = text.toString() != user.lastName.orEmpty()
            setDoneButtonVisibility(user.interests)
        }
    }

    private fun setupInterests(userInterests: List<UserInterest>) = with(binding.recyclerViewEditProfileInterests) {
        layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        addFlexboxSpaceItemDecoration(R.dimen.interests_list_spacing)
        adapter = interestsAdapter.apply {
            submitList(UserInterest.values().toList().map { ListInterest(it, userInterests.contains(it)) })
            onItemClick = { interest ->
                val interestsList = viewModel.selectedInterests
                if (interestsList.contains(interest)) {
                    interestsList.remove(interest)
                } else {
                    interestsList.add(interest)
                }
                setDoneButtonVisibility(userInterests)
            }
        }
    }

    private fun setDoneButtonVisibility(userInterests: List<UserInterest>) {
        binding.buttonEditProfileDone.isVisible = emailChanged || usernameChanged ||
            firstNameChanged || lastNameChanged || viewModel.selectedInterests.toSet() != userInterests.toSet()
    }
}
