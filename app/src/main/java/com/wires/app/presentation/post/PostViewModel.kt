package com.wires.app.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Comment
import com.wires.app.data.model.Post
import com.wires.app.data.model.SetLikeResult
import com.wires.app.domain.usecase.posts.CommentPostUseCase
import com.wires.app.domain.usecase.posts.GetPostCommentsUseCase
import com.wires.app.domain.usecase.posts.GetPostUseCase
import com.wires.app.domain.usecase.posts.LikePostUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val commentPostUseCase: CommentPostUseCase,
    private val likePostUseCase: LikePostUseCase
) : BaseViewModel() {

    private val _postLiveData = MutableLiveData<LoadableResult<Post>>()
    val postLiveData: LiveData<LoadableResult<Post>> = _postLiveData

    private val _commentsLiveData = MutableLiveData<PagingData<Comment>>()
    val commentsLiveData: LiveData<PagingData<Comment>> = _commentsLiveData

    private val _commentsStateLiveData = MutableLiveData<LoadableResult<Unit>>()
    val commentStateLiveData: LiveData<LoadableResult<Unit>> = _commentsStateLiveData

    private val _addCommentLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val addCommentLiveEvent: LiveData<LoadableResult<Unit>> = _addCommentLiveEvent

    private val _postLikeLiveEvent = SingleLiveEvent<LoadableResult<SetLikeResult>>()
    val postLikeLiveEvent: LiveData<LoadableResult<SetLikeResult>> = _postLikeLiveEvent

    private val _openProfileLiveEvent = SingleLiveEvent<Int>()
    val openProfileLiveEvent: LiveData<Int> = _openProfileLiveEvent

    fun getPost(postId: Int) {
        _postLiveData.launchLoadData(getPostUseCase.executeLoadable(GetPostUseCase.Params(postId)))
    }

    fun getComments(postId: Int) {
        _commentsLiveData.launchPagingData(getPostCommentsUseCase.execute(GetPostCommentsUseCase.Params(postId)))
    }

    fun addComment(postId: Int, text: String) {
        _addCommentLiveEvent.launchLoadData(commentPostUseCase.executeLoadable(CommentPostUseCase.Params(postId, text)))
    }

    fun setPostLike(postId: Int, isLiked: Boolean) {
        _postLikeLiveEvent.launchLoadData(likePostUseCase.executeLoadable(LikePostUseCase.Params(postId, isLiked)))
    }

    fun bindPagingState(state: CombinedLoadStates) {
        _commentsStateLiveData.bindPagingState(state)
    }

    fun openProfile(userId: Int) {
        _openProfileLiveEvent.postValue(userId)
    }
}
