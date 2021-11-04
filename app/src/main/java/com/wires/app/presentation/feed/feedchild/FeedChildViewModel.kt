package com.wires.app.presentation.feed.feedchild

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.presentation.base.BaseViewModel
import javax.inject.Inject

class FeedChildViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : BaseViewModel() {

    private val _postsLiveData = MutableLiveData<LoadableResult<List<Post>>>()
    val postsLiveData: LiveData<LoadableResult<List<Post>>> = _postsLiveData

    fun getPosts(interests: List<UserInterest>) {
        _postsLiveData.launchLoadData {
            postsRepository.getPosts(interests)
        }
    }
}
