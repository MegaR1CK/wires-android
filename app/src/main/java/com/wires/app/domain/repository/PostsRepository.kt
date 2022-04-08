package com.wires.app.domain.repository

import androidx.paging.PagingData
import com.wires.app.data.mapper.PostsMapper
import com.wires.app.data.model.Comment
import com.wires.app.data.model.CreatedPost
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.CommentAddParams
import com.wires.app.domain.paging.createPager
import com.wires.app.managers.MockManager
import com.wires.app.presentation.feed.feedchild.FeedPagingSource
import com.wires.app.presentation.post.CommentsPagingSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val mockManager: MockManager,
    private val apiService: WiresApiService,
    private val postsMapper: PostsMapper
) {
    suspend fun getPostsCompilation(interest: UserInterest?, limit: Int, offset: Int): List<Post> {
        delay(2000)
        return apiService.getPostsCompilation(interest?.value, limit, offset).data.map { postsMapper.fromResponseToModel(it) }
    }

    fun getPostsFlow(interest: UserInterest?): Flow<PagingData<Post>> {
        return createPager(FeedPagingSource(this, interest)).flow
    }

    suspend fun getPost(postId: Int): Post {
        return postsMapper.fromResponseToModel(apiService.getPost(postId).data)
    }

    suspend fun createPost(post: CreatedPost) {
        return mockManager.createPost(post)
    }

    suspend fun getComments(postId: Int, limit: Int, offset: Int): List<Comment> {
        return apiService.getPostComments(postId, limit, offset).data.map { postsMapper.fromResponseToModel(it) }
    }

    fun getCommentsFlow(postId: Int): Flow<PagingData<Comment>> {
        return createPager(CommentsPagingSource(this, postId)).flow
    }

    suspend fun commentPost(postId: Int, text: String) {
        apiService.commentPost(postId, CommentAddParams(text))
    }
}
