package com.wires.app.domain.repository

import androidx.paging.PagingData
import com.wires.app.data.mapper.PostsMapper
import com.wires.app.data.model.Comment
import com.wires.app.data.model.CreatedPost
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.data.remote.WiresApiService
import com.wires.app.domain.paging.createPager
import com.wires.app.managers.MockManager
import com.wires.app.presentation.feed.feedchild.FeedPagingSource
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
        return mockManager.getPost(postId)
    }

    suspend fun createPost(post: CreatedPost) {
        return mockManager.createPost(post)
    }

    suspend fun getComments(postId: Int): List<Comment> {
        return mockManager.getComments(postId)
    }

    suspend fun addComment(comment: Comment) {
        mockManager.addComment(comment)
    }
}
