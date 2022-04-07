package com.wires.app.presentation.feed.feedchild

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
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

    private val _postsLiveData = MutableLiveData<PagingData<Post>>()
    val postsLiveData: LiveData<PagingData<Post>> = _postsLiveData

    private val _postsLoadingStateLiveData = MutableLiveData<LoadableResult<Unit>>()
    val postsLoadingStateLiveData: LiveData<LoadableResult<Unit>> = _postsLoadingStateLiveData

    private val _openPostLiveEvent = SingleLiveEvent<Int>()
    val openPostLiveEvent: LiveData<Int> = _openPostLiveEvent

    fun getPosts(interest: UserInterest? = null) {
        _postsLiveData.launchPagingData {
            postsRepository.getPostsFlow(interest)
        }
    }

    fun bindLoadingState(state: CombinedLoadStates) {
        _postsLoadingStateLiveData.bindPagingState(state)
    }

    fun openPost(postId: Int) {
        _openPostLiveEvent.postValue(postId)
    }
}
