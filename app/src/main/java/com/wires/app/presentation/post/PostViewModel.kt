package com.wires.app.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Comment
import com.wires.app.data.model.Post
import com.wires.app.data.model.User
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import java.time.LocalDateTime
import javax.inject.Inject

class PostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _postLiveData = MutableLiveData<LoadableResult<Post>>()
    val postLiveData: LiveData<LoadableResult<Post>> = _postLiveData

    private val _commentsLiveData = MutableLiveData<LoadableResult<List<Comment>>>()
    val commentsLiveData: LiveData<LoadableResult<List<Comment>>> = _commentsLiveData

    private val _addCommentLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val addCommentLiveEvent: LiveData<LoadableResult<Unit>> = _addCommentLiveEvent

    private val _displayCommentLiveEvent = SingleLiveEvent<Comment>()
    val displayCommentLiveEvent: LiveData<Comment> = _displayCommentLiveEvent

    private val _userLiveData = MutableLiveData<LoadableResult<User>>()
    val userLiveData: LiveData<LoadableResult<User>> = _userLiveData

    fun getPost(postId: Int) {
        _postLiveData.launchLoadData { postsRepository.getPost(postId) }
    }

    fun getComments(postId: Int) {
        _commentsLiveData.launchLoadData { postsRepository.getComments(postId) }
    }

    fun getUser() {
        _userLiveData.launchLoadData { userRepository.getStoredUser() }
    }

    fun addComment(text: String) {
        _userLiveData.value?.getOrNull()?.let { user ->
            val comment = Comment(0, user, LocalDateTime.now(), text)
            _addCommentLiveEvent.launchLoadData { postsRepository.addComment(comment) }
            _displayCommentLiveEvent.postValue(comment)
        }
    }
}
