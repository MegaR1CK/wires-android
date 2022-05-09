package com.wires.app.presentation.createpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.usecase.posts.CreatePostUseCase
import com.wires.app.domain.usecase.posts.GetPostUseCase
import com.wires.app.domain.usecase.posts.UpdatePostUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase
) : BaseViewModel() {

    private val _proceedPostLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val proceedPostLiveEvent: LiveData<LoadableResult<Unit>> = _proceedPostLiveEvent

    private val _postLiveData = MutableLiveData<LoadableResult<Post>>()
    val postLiveData: LiveData<LoadableResult<Post>> = _postLiveData

    private val _selectTopicLiveEvent = SingleLiveEvent<Unit>()
    val selectTopicLiveEvent: LiveData<Unit> = _selectTopicLiveEvent

    var selectedImageUri: String? = null
    var selectedTopic: UserInterest? = null

    fun createPost(text: String) {
        selectedTopic?.let { topic ->
            _proceedPostLiveEvent.launchLoadData(
                createPostUseCase.executeLoadable(CreatePostUseCase.Params(text, topic, selectedImageUri))
            )
        }
    }

    fun updatePost(postId: Int, text: String?) {
        selectedTopic?.let { topic ->
            _proceedPostLiveEvent.launchLoadData(
                updatePostUseCase.executeLoadable(UpdatePostUseCase.Params(postId, text, topic, selectedImageUri))
            )
        }
    }

    fun getPost(postId: Int) {
        _postLiveData.launchLoadData(getPostUseCase.executeLoadable(GetPostUseCase.Params(postId)))
    }

    fun openTopicSelect() {
        _selectTopicLiveEvent.postValue(Unit)
    }
}
