package com.wires.app.presentation.createpost

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentCreatePostBinding
import com.wires.app.extensions.bytesEqualTo
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.handleImagePickerResult
import com.wires.app.extensions.load
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.pickImage
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CreatePostFragment : BaseFragment(R.layout.fragment_create_post) {

    companion object {
        const val POST_CHANGED_RESULT_KEY = "post_changed_key"
    }

    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by appViewModels()
    private val args: CreatePostFragmentArgs by navArgs()

    private val pickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        requireActivity().handleImagePickerResult(
            result = result,
            needPath = true,
            onSuccess = { path ->
                setImage(Uri.fromFile(File(path)).toString(), isInitial = false)
            },
            onFailure = { showSnackbar(getString(R.string.message_image_pick_error)) }
        )
    }

    private var isEditMode = false
    private var initialImage: Drawable? = null

    @Inject lateinit var appContext: Context

    override fun callOperations() {
        isEditMode = args.postId != 0
        if (isEditMode) viewModel.getPost(args.postId)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        if (!isEditMode) stateViewFlipperCreatePost.setStateFromResult(LoadableResult.success(null))
        toolbarCreatePost.setNavigationOnClickListener { navigateBack() }
        toolbarCreatePost.title = getString(if (isEditMode) R.string.create_post_title_edit else R.string.create_post_title)
        editTextCreatePost.doOnTextChanged { text, _, _, _ ->
            if (imageViewCreatePost.drawable == null && viewModel.selectedTopic != null) {
                buttonCreatePostDone.isVisible = !text.isNullOrBlank()
            }
        }
        buttonCreatePostDone.setOnClickListener {
            if (isEditMode) {
                viewModel.updatePost(args.postId, editTextCreatePost.getInputText())
            } else {
                viewModel.createPost(editTextCreatePost.getInputText())
            }
        }
        buttonCreatePostImageAdd.setOnClickListener {
            requireActivity().pickImage(pickerResultLauncher)
        }
        buttonCreatePostImageRemove.setOnClickListener { removeImage() }
        textViewTopicSelect.setOnClickListener { viewModel.openTopicSelect() }
        setFragmentResultListener(SelectTopicDialog.SELECT_INTEREST_RESULT_KEY) { _, bundle ->
            val topic = bundle.getSerializable(SelectTopicDialog.SELECTED_INTEREST_KEY) as? UserInterest
            topic?.let(::setPostTopic)
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        proceedPostLiveEvent.observe { result ->
            binding.progressIndicatorCreatePost.isVisible = result.isLoading
            result.doOnSuccess {
                setFragmentResult(POST_CHANGED_RESULT_KEY, bundleOf())
                navigateBack()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
        postLiveData.observe { result ->
            binding.stateViewFlipperCreatePost.setStateFromResult(result)
            result.doOnSuccess(::bindPostForEdit)
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        selectTopicLiveEvent.observe {
            navigateTo(CreatePostFragmentDirections.actionCreatePostFragmentToSelectTopicDialog())
        }
    }

    private fun setPostTopic(topic: UserInterest) = with(binding.textViewTopicSelect) {
        text = getString(R.string.create_post_topic, topic.value)
        setTextColor(requireContext().getColorAttribute(R.attr.textColorPrimary))
        viewModel.selectedTopic = topic
        binding.buttonCreatePostDone.isVisible =
            binding.imageViewCreatePost.drawable != null || binding.editTextCreatePost.getInputText().isNotBlank()
    }

    private fun setImage(imageUri: String, isInitial: Boolean) = with(binding) {
        buttonCreatePostImageAdd.isVisible = false
        buttonCreatePostImageRemove.isVisible = true
        imageViewCreatePost.run {
            load(
                imageUrl = imageUri,
                doOnSuccess = { drawable -> setImageUri(imageUri, drawable, isInitial) }
            )
            isVisible = true
        }

        buttonCreatePostDone.isVisible = viewModel.selectedTopic != null
    }

    private fun setImageUri(imageUri: String, drawable: Drawable?, isInitial: Boolean) {
        viewModel.selectedImageUri = if (isEditMode) {
            when {
                isInitial -> {
                    initialImage = drawable
                    null
                }
                initialImage?.bytesEqualTo(drawable) == true -> null
                else -> imageUri
            }
        } else imageUri
    }

    private fun removeImage() = with(binding) {
        viewModel.selectedImageUri = if (initialImage != null) "" else null
        buttonCreatePostImageAdd.isVisible = true
        buttonCreatePostImageRemove.isVisible = false
        imageViewCreatePost.run {
            setImageDrawable(null)
            isVisible = false
        }
        if (editTextCreatePost.getInputText().isBlank()) buttonCreatePostDone.isVisible = false
    }

    private fun bindPostForEdit(post: Post) = with(binding) {
        setPostTopic(post.topic)
        editTextCreatePost.setText(post.text)
        post.image?.url?.let { setImage(it, isInitial = true) }
    }
}
