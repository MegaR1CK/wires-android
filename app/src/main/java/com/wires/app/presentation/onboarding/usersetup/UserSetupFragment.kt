package com.wires.app.presentation.onboarding.usersetup

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentUserSetupBinding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.handleImagePickerResult
import com.wires.app.extensions.pickImage
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.onboarding.OnSubmitListener

class UserSetupFragment : BaseFragment(R.layout.fragment_user_setup) {

    private val binding by viewBinding(FragmentUserSetupBinding::bind)
    private val viewModel: UserSetupViewModel by appViewModels()

    private val pickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        requireActivity().handleImagePickerResult(
            result = result,
            onSuccess = { stringUri ->
                val uri = Uri.parse(stringUri)
                selectedAvatarUri = uri
                viewModel.selectedAvatarPath = uri.path
            },
            onComplete = { setAvatarUri() },
            onFailure = { showSnackbar(getString(R.string.message_image_pick_error)) }
        )
    }

    private var onSubmitListener: OnSubmitListener? = null
    private var selectedAvatarUri: Uri? = null

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        buttonOnboardingContinue.setOnClickListener { submitUserData() }
        viewImagePickerOnboarding.setOnClickListener {
            requireActivity().pickImage(pickerResultLauncher, needCrop = true)
        }
        inputOnboardingLastName.setOnEditorActionListener {
            submitUserData()
            true
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        submitDataLiveEvent.observe { data ->
            onSubmitListener?.onSubmitUserData(data)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onSubmitListener = parentFragment as? OnSubmitListener
    }

    private fun setAvatarUri() = with(binding) {
        imageViewOnboardingAvatar.setImageURI(selectedAvatarUri)
        imageViewOnboardingIcon.setColorFilter(
            requireContext().getColorAttribute(if (selectedAvatarUri != null) R.attr.iconColorOnContrast else R.attr.colorPrimary)
        )
    }

    private fun submitUserData() = with(binding) {
        val firstNameValidated = inputOnboardingFirstName.validate()
        val lastNameValidated = inputOnboardingLastName.validate()
        if (firstNameValidated && lastNameValidated) {
            viewModel.submitData(inputOnboardingFirstName.text.orEmpty(), inputOnboardingLastName.text.orEmpty())
        }
    }
}
