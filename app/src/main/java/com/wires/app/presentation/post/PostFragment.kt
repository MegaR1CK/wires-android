package com.wires.app.presentation.post

import android.animation.AnimatorInflater
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.databinding.FragmentPostBinding
import com.wires.app.domain.paging.PagingLoadStateAdapter
import com.wires.app.extensions.countViewHeight
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getKeyboardInset
import com.wires.app.extensions.load
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showToast
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class PostFragment : BaseFragment(R.layout.fragment_post) {

    companion object {
        const val LIKE_CHANGED_RESULT_KEY = "result_like_changed"
        const val POST_ID_RESULT_KEY = "result_post_id"
    }

    private val viewModel: PostViewModel by appViewModels()
    private val args: PostFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentPostBinding::bind)

    /**
     * Маркер для того, чтобы отличать, обновляются ли комментарии по свайпу, или по запросу пагинации
     */
    private var isRefreshingBySwipe = false

    /**
     * Маркер первой загрузки списка комментариев
     */
    private var isFirstLoading = true

    @Inject lateinit var commentsAdapter: CommentsAdapter
    @Inject lateinit var dateFormatter: DateFormatter

    override fun callOperations() {
        viewModel.getPost(args.postId)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        // Необходимо для того, чтобы при открытии клавиатуры комментарии скроллились в начало
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            if (insets.getKeyboardInset() > 0) nestedScrollViewPost.smoothScrollTo(0, 0)
        }
        toolbarPost.setNavigationOnClickListener { findNavController().popBackStack() }
        recyclerViewPostComments.adapter = commentsAdapter.apply {
            addLoadStateListener(viewModel::bindPagingState)
        }.withLoadStateFooter(PagingLoadStateAdapter { commentsAdapter.retry() })
        messageInputViewComment.setOnSendClickListener { text ->
            viewModel.addComment(args.postId, text)
        }
        swipeRefreshLayoutPost.setOnRefreshListener {
            viewModel.getComments(args.postId)
            isRefreshingBySwipe = true
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
        commentsLiveData.observe { data ->
            commentsAdapter.submitData(lifecycle, data)
        }
        commentStateLiveData.observe { result ->
            when {
                isFirstLoading -> {
                    binding.stateViewFlipperPost.setStateFromResult(result)
                    result.doOnSuccess { isFirstLoading = false }
                }
                isRefreshingBySwipe -> {
                    binding.swipeRefreshLayoutPost.isRefreshing = result.isLoading
                    result.doOnComplete { isRefreshingBySwipe = false }
                    result.doOnFailure { error ->
                        showToast(error.title)
                        Timber.e(error.title)
                    }
                }
                binding.messageInputViewComment.isLoadingState -> {
                    binding.messageInputViewComment.handleResult(LoadableResult.success(result))
                }
            }
        }
        addCommentLiveEvent.observe { result ->
            if (!result.isSuccess) binding.messageInputViewComment.handleResult(result)
            result.doOnSuccess {
                viewModel.getComments(args.postId)
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
        postLikeLiveEvent.observe { result ->
            result.doOnSuccess { likeResult ->
                likeResult.error?.let { error ->
                    switchLikeState()
                    Timber.e(error.message)
                    showSnackbar(error.message)
                } ?: run {
                    val initialLikeValue = postLiveData.value?.getOrNull()?.isLiked
                    val currentLikeValue = binding.viewPost.imageViewPostLike.isSelected
                    // Ставим non-null result только если состояние лайка отличается от изначального
                    setFragmentResult(
                        requestKey = LIKE_CHANGED_RESULT_KEY,
                        result = bundleOf(
                            POST_ID_RESULT_KEY to if (initialLikeValue != currentLikeValue) likeResult.postId else 0
                        )
                    )
                }
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
        openProfileLiveEvent.observe { userId ->
            findNavController().navigate(PostFragmentDirections.actionPostFragmentToProfileGraph(userId))
        }
    }

    private fun bindPost(post: Post) = with(binding.viewPost) {
        if (!post.author.firstName.isNullOrEmpty() && !post.author.lastName.isNullOrEmpty()) {
            textViewPostAuthor.text = requireContext().getString(
                R.string.user_full_name,
                post.author.firstName,
                post.author.lastName
            )
        } else {
            textViewPostAuthor.text = post.author.username
        }
        textVewPostTime.text = dateFormatter.dateTimeToStringRelative(post.publishTime)
        imageViewPostAuthorAvatar.load(
            imageUrl = post.author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewPostBody.text = post.text
        post.image?.let { image ->
            imageViewPostImage.countViewHeight(image.size.width, image.size.height)
            imageViewPostImage.load(image.url)
        }
        textViewPostLikeCounter.text = post.likesCount.toString()
        textViewPostCommentCounter.text = post.commentsCount.toString()
        constraintLayoutPostAuthor.setOnClickListener { viewModel.openProfile(post.author.id) }
        imageViewPostLike.isSelected = post.isLiked
        linearLayoutPostLike.setOnClickListener {
            switchLikeState()
            viewModel.setPostLike(post.id, imageViewPostLike.isSelected)
            val likeAnimator = AnimatorInflater.loadAnimator(context, R.animator.anim_like_button)
            likeAnimator.setTarget(imageViewPostLike)
            likeAnimator.start()
        }
    }

    private fun switchLikeState() = with(binding.viewPost) {
        imageViewPostLike.isSelected = !imageViewPostLike.isSelected
        val likesCount = textViewPostLikeCounter.text.toString().toInt()
        textViewPostLikeCounter.text = if (imageViewPostLike.isSelected) {
            likesCount.inc().toString()
        } else {
            likesCount.dec().toString()
        }
    }
}
