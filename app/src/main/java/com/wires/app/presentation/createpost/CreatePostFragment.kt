package com.wires.app.presentation.createpost

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wires.app.R
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentCreatePostBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class CreatePostFragment : BaseFragment(R.layout.fragment_create_post) {

    companion object {
        const val POST_CREATED_RESULT_KEY = "post_created_key"
    }

    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by appViewModels()

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.selectedImagePath = uri.path
                setImage(uri)
            }
        }
    }

    @Inject lateinit var appContext: Context

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        toolbarCreatePost.setNavigationOnClickListener { findNavController().popBackStack() }
        editTextCreatePost.doOnTextChanged { text, _, _, _ ->
            if (imageViewCreatePost.drawable == null) buttonCreatePostDone.isVisible = !text.isNullOrBlank()
        }
        buttonCreatePostDone.setOnClickListener {
            viewModel.createPost(editTextCreatePost.getInputText(), UserInterest.ANDROID_DEVELOPMENT)
        }
        buttonCreatePostImageAdd.setOnClickListener { startImagePicker() }
        buttonCreatePostImageRemove.setOnClickListener { removeImage() }
    }

    override fun onBindViewModel() = with(viewModel) {
        createPostLiveEvent.observe { result ->
            binding.progressIndicatorCreatePost.isVisible = result.isLoading
            result.doOnSuccess {
                setFragmentResult(POST_CREATED_RESULT_KEY, bundleOf())
                findNavController().popBackStack()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
    }

    private fun setImage(imageUri: Uri) = with(binding) {
        buttonCreatePostImageAdd.isVisible = false
        buttonCreatePostImageRemove.isVisible = true
        imageViewCreatePost.run {
            setImageURI(imageUri)
            isVisible = true
        }
        if (editTextCreatePost.getInputText().isBlank()) buttonCreatePostDone.isVisible = true
    }

    private fun removeImage() = with(binding) {
        viewModel.selectedImagePath = null
        buttonCreatePostImageAdd.isVisible = true
        buttonCreatePostImageRemove.isVisible = false
        imageViewCreatePost.run {
            setImageDrawable(null)
            isVisible = false
        }
        if (editTextCreatePost.getInputText().isBlank()) buttonCreatePostDone.isVisible = false
    }

    private fun startImagePicker() {
        ImagePicker
            .with(requireActivity())
            .createIntent { intent ->
                resultLauncher.launch(intent)
            }
    }
}
