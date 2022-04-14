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

    var selectedImagePath: String? = null

    fun createPost(text: String, topic: UserInterest) {
        _createPostLiveEvent.launchLoadData(
            createPostUseCase.executeLoadable(CreatePostUseCase.Params(text, topic, selectedImagePath))
        )
    }
}
