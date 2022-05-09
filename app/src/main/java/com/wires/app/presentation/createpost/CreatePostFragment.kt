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
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.databinding.FragmentCreatePostBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.load
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CreatePostFragment : BaseFragment(R.layout.fragment_create_post) {

    companion object {
        const val POST_CHANGED_RESULT_KEY = "post_created_key"
        private const val FILE_PATH_KEY = "extra.file_path"
    }

    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by appViewModels()
    private val args: CreatePostFragmentArgs by navArgs()

    private var isEditMode = false
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.extras?.getString(FILE_PATH_KEY)?.let { path ->
                viewModel.selectedImagePath = path
                setImage(Uri.fromFile(File(path)).toString())
            }
        }
    }

    @Inject lateinit var appContext: Context

    override fun callOperations() {
        if (args.postId != 0) viewModel.getPost(args.postId)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        isEditMode = args.postId != 0
        root.fitKeyboardInsetsWithPadding()
        if (!isEditMode) stateViewFlipperCreatePost.setStateFromResult(LoadableResult.success(null))
        toolbarCreatePost.setNavigationOnClickListener { navigateBack() }
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
        buttonCreatePostImageAdd.setOnClickListener { startImagePicker() }
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

    private fun setImage(imageUri: String) = with(binding) {
        buttonCreatePostImageAdd.isVisible = false
        buttonCreatePostImageRemove.isVisible = true
        imageViewCreatePost.run {
            load(imageUri)
            isVisible = true
        }
        buttonCreatePostDone.isVisible = viewModel.selectedTopic != null
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

    private fun bindPostForEdit(post: Post) = with(binding) {
        setPostTopic(post.topic)
        editTextCreatePost.setText(post.text)
        post.image?.url?.let { setImage(it) }
    }
}
