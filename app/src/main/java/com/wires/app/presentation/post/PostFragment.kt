package com.wires.app.presentation.post

import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.Post
import com.wires.app.databinding.FragmentPostBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.getKeyboardInset
import com.wires.app.extensions.load
import com.wires.app.extensions.scrollToEnd
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class PostFragment : BaseFragment(R.layout.fragment_post) {

    companion object {
        const val POST_RETURN_KEY = "post_return"
    }

    private val viewModel: PostViewModel by appViewModels()
    private val args: PostFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentPostBinding::bind)

    @Inject lateinit var commentsAdapter: CommentsAdapter
    @Inject lateinit var dateFormatter: DateFormatter

    override fun callOperations() {
        viewModel.getPost(args.postId)
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            if (insets.getKeyboardInset() > 0) nestedScrollViewPost.scrollToEnd()
        }
        toolbarPost.setNavigationOnClickListener { onBackPressed() }
        activity?.onBackPressedDispatcher?.addCallback { onBackPressed() }
        recyclerViewPostComments.adapter = commentsAdapter
        editTextPostComment.doOnTextChanged { text, _, _, _ ->
            buttonPostCommentSend.isInvisible = text.isNullOrBlank()
        }
        buttonPostCommentSend.setOnClickListener {
            viewModel.addComment(editTextPostComment.getInputText())
        }
        swipeRefreshLayoutPost.setOnRefreshListener {
            viewModel.getComments(args.postId)
        }
        swipeRefreshLayoutPost.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
    }

    override fun onBindViewModel() = with(viewModel) {
        postLiveData.observe { result ->
            if (!result.isSuccess) binding.stateViewFlipperPost.setStateFromResult(result)
            result.doOnSuccess { post ->
                bindPost(post)
                viewModel.getComments(post.id)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        commentsLiveData.observe { result ->
            (result.isLoading && commentsAdapter.itemCount != 0).let {
                binding.swipeRefreshLayoutPost.isRefreshing = it
                if (!it) binding.stateViewFlipperPost.setStateFromResult(result)
            }
            result.doOnSuccess { items ->
                commentsAdapter.submitList(items)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        addCommentLiveEvent.observe { result ->
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        displayCommentLiveEvent.observe { comment ->
            commentsAdapter.addItem(comment)
            binding.editTextPostComment.text = null
            binding.nestedScrollViewPost.post { binding.nestedScrollViewPost.scrollToEnd() }
        }
        userLiveData.observe { result ->
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }

    private fun bindPost(post: Post) = with(binding.viewPost) {
        if (!post.author.firstName.isNullOrEmpty() && !post.author.lastName.isNullOrEmpty()) {
            textViewPostAuthor.text = requireContext().getString(
                R.string.feed_post_author_name,
                post.author.firstName,
                post.author.lastName
            )
        } else {
            textViewPostAuthor.text = post.author.username
        }
        textVewPostTime.text = dateFormatter.dateTimeToStringRelative(post.publishTime)
        imageViewPostAuthorAvatar.load(
            post.author.avatarUrl,
            isCircle = true
        )
        textViewPostBody.text = post.text
        imageViewPostImage.load(post.imageUrl)
        textViewPostLikeCounter.text = post.likesCount.toString()
        textViewPostCommentCounter.text = post.commentsCount.toString()
    }

    private fun onBackPressed() {
        setFragmentResult(POST_RETURN_KEY, bundleOf())
        findNavController().popBackStack()
    }
}
