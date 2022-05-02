package com.wires.app.presentation.edituser

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wires.app.R
import com.wires.app.data.model.ListInterest
import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentEditUserBinding
import com.wires.app.extensions.addFlexboxSpaceItemDecoration
import com.wires.app.extensions.addOrRemove
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
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

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedAvatarUri = uri
                viewModel.selectedAvatarPath = uri.path
                binding.imageViewEditProfileAvatar.load(
                    imageUrl = uri.toString(),
                    placeHolderRes = R.drawable.ic_avatar_placeholder,
                    isCircle = true
                )
                setDoneButtonVisibility()
            }
        } else {
            binding.imageViewEditProfileAvatar.setImageURI(selectedAvatarUri)
        }
    }

    private var selectedAvatarUri: Uri? = null
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
        inputEditProfileEmail.validationRegex = Patterns.EMAIL_ADDRESS.toRegex()
        toolbarEditProfile.setNavigationOnClickListener { findNavController().popBackStack() }
        buttonEditProfileDone.setOnClickListener {
            val usernameValidated = inputEditProfileUsername.validate()
            val emailValidated = inputEditProfileEmail.validate()
            val firstNameValidated = inputEditProfileFirstName.validate()
            val lastNameValidated = inputEditProfileLastName.validate()
            if (usernameValidated && emailValidated && firstNameValidated && lastNameValidated) {
                viewModel.updateUser(
                    username = inputEditProfileUsername.text,
                    email = inputEditProfileEmail.text,
                    firstName = inputEditProfileFirstName.text,
                    lastName = inputEditProfileLastName.text,
                )
            }
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
    }

    private fun setupUser(user: User) = with(binding) {
        imageViewEditProfileAvatar.setOnClickListener {
            ImagePicker
                .with(requireActivity())
                .cropSquare()
                .createIntent { intent ->
                    resultLauncher.launch(intent)
                }
        }
        imageViewEditProfileAvatar.load(
            imageUrl = user.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        inputEditProfileFirstName.text = user.firstName
        inputEditProfileLastName.text = user.lastName
        inputEditProfileEmail.text = user.email
        inputEditProfileUsername.text = user.username
        viewModel.selectedInterests.addAll(user.interests)
        setupInterests(user.interests)
        inputEditProfileEmail.editText.doOnTextChanged { text, _, _, _ ->
            emailChanged = text.toString() != user.email
            setDoneButtonVisibility(user.interests)
        }
        inputEditProfileUsername.editText.doOnTextChanged { text, _, _, _ ->
            usernameChanged = text.toString() != user.username
            setDoneButtonVisibility(user.interests)
        }
        inputEditProfileFirstName.editText.doOnTextChanged { text, _, _, _ ->
            firstNameChanged = text.toString() != user.firstName.orEmpty()
            setDoneButtonVisibility(user.interests)
        }
        inputEditProfileLastName.editText.doOnTextChanged { text, _, _, _ ->
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
                viewModel.selectedInterests.addOrRemove(interest)
                setDoneButtonVisibility(userInterests)
            }
        }
    }

    private fun setDoneButtonVisibility(userInterests: List<UserInterest>? = null) {
        binding.buttonEditProfileDone.isVisible = emailChanged || usernameChanged || firstNameChanged ||
            lastNameChanged || viewModel.selectedInterests.toSet() != userInterests?.toSet() || selectedAvatarUri != null
    }
}
