package com.wires.app.presentation.feed.feedchild

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class FeedChildViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : BaseViewModel() {

    /** Данные о постах. SingleLiveEvent, чтобы при возвращении с
     * CreatePostFragment посты не загружались в список заново после очистки */
    private val _postsLiveData = SingleLiveEvent<LoadableResult<List<Post>>>()
    val postsLiveData: LiveData<LoadableResult<List<Post>>> = _postsLiveData

    private val _openPostLiveEvent = SingleLiveEvent<Int>()
    val openPostLiveEvent: LiveData<Int> = _openPostLiveEvent

    fun getPosts(interests: List<UserInterest>) {
        _postsLiveData.launchLoadData {
            postsRepository.getPosts(interests)
        }
    }

    fun openPost(postId: Int) {
        _openPostLiveEvent.postValue(postId)
    }
}
