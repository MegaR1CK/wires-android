package com.wires.app.presentation.post

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.databinding.FragmentPostBinding
import com.wires.app.domain.paging.PagingLoadStateAdapter
import com.wires.app.extensions.countViewHeight
import com.wires.app.extensions.createLoadableResultDialog
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.getKeyboardInset
import com.wires.app.extensions.load
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showAlertDialog
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showToast
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.createpost.CreatePostFragment
import timber.log.Timber
import javax.inject.Inject

class PostFragment : BaseFragment(R.layout.fragment_post) {

    companion object {
        const val LIKE_CHANGED_RESULT_KEY = "result_like_changed"
        const val POST_ID_RESULT_KEY = "result_post_id"
        const val COMMENTS_CHANGED_RESULT_KEY = "result_comments_changed"
        const val COMMENTS_COUNT_RESULT_KEY = "result_comments_count"
        const val POST_UPDATED_RESULT_KEY = "result_post_updated"
        const val POST_DATA_RESULT_KEY = "result_post_data"
        const val POST_DELETED_RESULT_KEY = "result_post_deleted"
    }

    private val viewModel: PostViewModel by appViewModels()
    private val args: PostFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentPostBinding::bind)

    /**
     * Маркер для того, чтобы отличать, обновляются ли комментарии по свайпу, или по запросу пагинации
     */
    private var isRefreshingBySwipe = false

    /**
     * Маркер первоначальной (полноэкранной) загрузки
     */
    private var initialLoading = true

    /**
     * Маркер требования результата обновленного поста, нужен для выставления fragmentResult
     * после загрузки обновленного поста. Сбрасывается после выставления fragmentResult.
     */
    private var needUpdateResult = false

    /**
     * Маркер обновленного поста, нужен для изменения поведения обсерверов сразу после возвращения, когда эмитятся
     * LoadableResult Success. Сбрасывается после прогона Success в последнем обсервере (Comments state)
     */
    private var postUpdated = false

    @Inject lateinit var commentsAdapter: CommentsAdapter
    @Inject lateinit var dateFormatter: DateFormatter

    override fun callOperations() {
        viewModel.getPost(args.postId)
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        // Необходимо для того, чтобы при открытии клавиатуры комментарии скроллились в начало
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            if (insets.getKeyboardInset() > 0) nestedScrollViewPost.smoothScrollTo(0, 0)
        }
        toolbarPost.setNavigationOnClickListener { navigateBack() }
        recyclerViewPostComments.adapter = commentsAdapter.apply {
            addLoadStateListener(viewModel::bindPagingState)
        }.withLoadStateFooter(PagingLoadStateAdapter { commentsAdapter.retry() })
        messageInputViewComment.setOnSendClickListener { text ->
            viewModel.addComment(args.postId, text)
        }
        stateViewFlipperPost.setRetryMethod(::callOperations)
        swipeRefreshLayoutPost.setOnRefreshListener {
            viewModel.getPost(args.postId)
            isRefreshingBySwipe = true
        }
        swipeRefreshLayoutPost.setColorSchemeColors(requireContext().getColorAttribute(R.attr.colorPrimary))
        setFragmentResultListener(CreatePostFragment.POST_CHANGED_RESULT_KEY) { _, _ ->
            needUpdateResult = true
            initialLoading = true
            postUpdated = true
            callOperations()
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        userLiveData.observe { result ->
            result.doOnSuccess { wrapper ->
                val postResult = postLiveData.value
                if (postResult?.isSuccess == true) {
                    binding.viewPost.buttonPostActions.isVisible = wrapper.user?.id == postResult.getOrNull()?.author?.id
                }
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
        postLiveData.observe { result ->
            if (initialLoading && !result.isSuccess || (result.isSuccess && !initialLoading)) {
                binding.stateViewFlipperPost.setStateFromResult(result)
            }
            if (isRefreshingBySwipe) {
                if (!result.isSuccess) binding.swipeRefreshLayoutPost.isRefreshing = result.isLoading
                result.doOnFailure { error ->
                    showToast(error.message)
                }
            }
            result.doOnSuccess { post ->
                if ((initialLoading && !postUpdated) || isRefreshingBySwipe) viewModel.getComments(post.id)
                if (needUpdateResult && !postUpdated) {
                    setFragmentResult(POST_UPDATED_RESULT_KEY, bundleOf(POST_DATA_RESULT_KEY to post))
                    needUpdateResult = false
                }
                bindPost(post.copy(isEditable = userLiveData.value?.getOrNull()?.user?.id == post.author.id))
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        commentsLiveData.observe { data ->
            commentsAdapter.submitData(lifecycle, data)
        }
        commentStateLiveData.observe { result ->
            if (!postUpdated) {
                when {
                    initialLoading -> {
                        binding.stateViewFlipperPost.setStateFromResult(result)
                        result.doOnSuccess {
                            initialLoading = false
                        }
                    }
                    isRefreshingBySwipe -> {
                        binding.swipeRefreshLayoutPost.isRefreshing = result.isLoading
                        result.doOnComplete { isRefreshingBySwipe = false }
                        result.doOnFailure { error ->
                            showToast(error.title)
                        }
                    }
                    binding.messageInputViewComment.isLoadingState -> {
                        binding.messageInputViewComment.handleResult(LoadableResult.success(result))
                    }
                }
            } else {
                postUpdated = false
            }
            result.doOnFailure { error ->
                Timber.e(error.title)
            }
        }
        addCommentLiveEvent.observe { result ->
            if (!result.isSuccess) binding.messageInputViewComment.handleResult(result)
            result.doOnSuccess {
                viewModel.getComments(args.postId)
                with(binding.viewPost.textViewPostCommentCounter) {
                    val newCommentsCount = text.toString().toInt().inc()
                    text = newCommentsCount.toString()
                    setCommentsResult(newCommentsCount)
                }
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
        val loadableResultDialog = createLoadableResultDialog()
        postDeleteLiveEvent.observe { result ->
            loadableResultDialog.setState(result)
            result.doOnSuccess { postId ->
                setFragmentResult(POST_DELETED_RESULT_KEY, bundleOf(POST_ID_RESULT_KEY to postId))
                navigateBack()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
        openProfileLiveEvent.observe { userId ->
            navigateTo(PostFragmentDirections.actionPostFragmentToProfileGraph(userId))
        }
        openCreatePostLiveEvent.observe { postId ->
            navigateTo(PostFragmentDirections.actionPostFragmentToCreatePostGraph(postId))
        }
    }

    private fun bindPost(post: Post) = with(binding.viewPost) {
        textViewPostAuthor.text = post.author.getDisplayName()
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
        post.commentsCount.let { count ->
            textViewPostCommentCounter.text = count.toString()
            setCommentsResult(count)
        }
        constraintLayoutPostAuthor.setOnClickListener { viewModel.openProfile(post.author.id) }
        imageViewPostLike.isSelected = post.isLiked
        linearLayoutPostLike.setOnClickListener {
            switchLikeState()
            viewModel.setPostLike(post.id, imageViewPostLike.isSelected)
            val likeAnimator = AnimatorInflater.loadAnimator(context, R.animator.anim_like_button)
            likeAnimator.setTarget(imageViewPostLike)
            likeAnimator.start()
        }
        buttonPostActions.isVisible = post.isEditable
        buttonPostActions.setOnClickListener { showPopupMenu(it, post.id) }
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

    private fun setCommentsResult(commentsCount: Int) =
        setFragmentResult(
            COMMENTS_CHANGED_RESULT_KEY,
            bundleOf(
                POST_ID_RESULT_KEY to args.postId,
                COMMENTS_COUNT_RESULT_KEY to commentsCount
            )
        )

    private fun showPopupMenu(view: View, postId: Int) {
        PopupMenu(requireContext(), view).run {
            inflate(R.menu.menu_post_actions)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.postActionEdit -> viewModel.openCreatePost(postId)
                    R.id.postActionDelete -> {
                        showAlertDialog(
                            titleRes = R.string.dialog_post_delete_title,
                            messageRes = R.string.dialog_post_delete_message,
                            positiveButtonTextRes = R.string.dialog_yes,
                            negativeButtonTextRes = R.string.dialog_no,
                            positiveButtonListener = { dialog, _ ->
                                viewModel.deletePost(postId)
                                dialog.dismiss()
                            }
                        )
                    }
                }
                true
            }
            show()
        }
    }
}
