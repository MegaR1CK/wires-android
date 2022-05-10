package com.wires.app.presentation.feed.feedchild

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.SetLikeResult
import com.wires.app.data.model.UserInterest
import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.usecase.posts.DeletePostUseCase
import com.wires.app.domain.usecase.posts.GetFeedUseCase
import com.wires.app.domain.usecase.posts.LikePostUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.extensions.mapPagingValue
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class FeedChildViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : BaseViewModel() {

    private val _postsLiveData = MutableLiveData<PagingData<Post>>()
    val postsLiveData: LiveData<PagingData<Post>> = _postsLiveData

    private val _postsLoadingStateLiveData = MutableLiveData<LoadableResult<Unit>>()
    val postsLoadingStateLiveData: LiveData<LoadableResult<Unit>> = _postsLoadingStateLiveData

    private val _postLikeLiveEvent = SingleLiveEvent<LoadableResult<SetLikeResult>>()
    val postLikeLiveEvent: LiveData<LoadableResult<SetLikeResult>> = _postLikeLiveEvent

    private val _postDeleteLiveEvent = SingleLiveEvent<LoadableResult<Int>>()
    val postDeleteLiveEvent: LiveData<LoadableResult<Int>> = _postDeleteLiveEvent

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _openPostLiveEvent = SingleLiveEvent<Int>()
    val openPostLiveEvent: LiveData<Int> = _openPostLiveEvent

    private val _openProfileLiveEvent = SingleLiveEvent<Int>()
    val openProfileLiveEvent: LiveData<Int> = _openProfileLiveEvent

    fun getPosts(interest: UserInterest? = null) {
        _postsLiveData.launchPagingData(
            getFeedUseCase.execute(GetFeedUseCase.Params(interest)).mapPagingValue { post ->
                post.copy(isEditable = _userLiveData.value?.getOrNull()?.user?.id == post.author.id)
            }
        )
    }

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun setPostLike(postId: Int, isLiked: Boolean) {
        _postLikeLiveEvent.launchLoadData(likePostUseCase.executeLoadable(LikePostUseCase.Params(postId, isLiked)))
    }

    fun deletePost(postId: Int) {
        _postDeleteLiveEvent.launchLoadData(deletePostUseCase.executeLoadable(DeletePostUseCase.Params(postId)))
    }

    fun bindLoadingState(state: CombinedLoadStates) {
        _postsLoadingStateLiveData.bindPagingState(state)
    }

    fun openPost(postId: Int) {
        _openPostLiveEvent.postValue(postId)
    }

    fun openProfile(userId: Int) {
        _openProfileLiveEvent.postValue(userId)
    }
}
