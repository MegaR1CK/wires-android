package com.wires.app.presentation.createpost

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.usecase.posts.CreatePostUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : BaseViewModel() {

    private val _createPostLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val createPostLiveEvent: LiveData<LoadableResult<Unit>> = _createPostLiveEvent

    private val _selectTopicLiveEvent = SingleLiveEvent<Unit>()
    val selectTopicLiveEvent: LiveData<Unit> = _selectTopicLiveEvent

    var selectedImagePath: String? = null
    var selectedTopic: UserInterest? = null

    fun createPost(text: String) {
        selectedTopic?.let { topic ->
            _createPostLiveEvent.launchLoadData(
                createPostUseCase.executeLoadable(CreatePostUseCase.Params(text, topic, selectedImagePath))
            )
        }
    }

    fun openTopicSelect() {
        _selectTopicLiveEvent.postValue(Unit)
    }
}
