package com.wires.app.presentation.createpost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.wires.app.R
import com.wires.app.databinding.FragmentCreatePostBinding
import com.wires.app.extensions.errorSnackbar
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class CreatePostFragment : BaseFragment(R.layout.fragment_create_post) {

    companion object {
        const val POST_CREATED_RESULT_KEY = "post_created_key"
    }

    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by appViewModels()

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        toolbarCreatePost.setNavigationOnClickListener { findNavController().popBackStack() }
        editTextCreatePost.doOnTextChanged { text, _, _, _ ->
            if (imageViewCreatePost.drawable == null) buttonCreatePostDone.isVisible = !text.isNullOrBlank()
        }
        buttonCreatePostDone.setOnClickListener {
            viewModel.createPost(editTextCreatePost.getInputText())
        }
        buttonCreatePostImageAdd.setOnClickListener { startImagePicker() }
        buttonCreatePostImageRemove.setOnClickListener { removeImage() }
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { uri -> setImage(uri) }
            }
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        userLiveData.observe { result ->
            result.doOnFailure { error ->
                Timber.e(error.message)
                errorSnackbar(error.message)
            }
        }
        createPostLiveEvent.observe { result ->
            binding.progressIndicatorCreatePost.isVisible = result.isLoading
            result.doOnSuccess {
                setFragmentResult(POST_CREATED_RESULT_KEY, bundleOf())
                findNavController().popBackStack()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                errorSnackbar(error.message)
            }
        }
    }

    private fun setImage(uri: Uri) = with(binding) {
        buttonCreatePostImageAdd.isVisible = false
        buttonCreatePostImageRemove.isVisible = true
        imageViewCreatePost.run {
            setImageBitmap(getBitmapFromUri(uri))
            isVisible = true
        }
        if (editTextCreatePost.getInputText().isBlank()) buttonCreatePostDone.isVisible = true
    }

    private fun removeImage() = with(binding) {
        buttonCreatePostImageAdd.isVisible = true
        buttonCreatePostImageRemove.isVisible = false
        imageViewCreatePost.run {
            setImageDrawable(null)
            isVisible = false
        }
        if (editTextCreatePost.getInputText().isBlank()) buttonCreatePostDone.isVisible = false
    }

    private fun startImagePicker() {
        ImagePicker.with(requireActivity()).createIntentFromDialog {
            resultLauncher?.launch(it)
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val stream = requireActivity().contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(stream).also { stream?.close() }
    }
}
