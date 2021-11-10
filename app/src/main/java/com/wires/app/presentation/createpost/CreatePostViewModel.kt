package com.wires.app.presentation.createpost

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.CreatedPost
import com.wires.app.data.model.User
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class CreatePostViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postsRepository: PostsRepository
) : BaseViewModel() {

    private val _userLiveData = MutableLiveData<LoadableResult<User>>()
    val userLiveData: LiveData<LoadableResult<User>> = _userLiveData

    private val _createPostLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val createPostLiveEvent: LiveData<LoadableResult<Unit>> = _createPostLiveEvent

    fun getUser() {
        _userLiveData.launchLoadData { userRepository.getStoredUser() }
    }

    fun createPost(text: String, image: Bitmap? = null) {
        _userLiveData.value?.getOrNull()?.let { user ->
            _createPostLiveEvent.launchLoadData {
                postsRepository.createPost(
                    CreatedPost(
                        author = user,
                        text = text,
                        imageBitmap = image
                    )
                )
            }
        }
    }
}
