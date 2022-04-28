package com.wires.app.domain.repository

import androidx.paging.PagingData
import com.google.gson.Gson
import com.wires.app.data.mapper.PostsMapper
import com.wires.app.data.model.Comment
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.CommentAddParams
import com.wires.app.data.remote.params.PostCreateParams
import com.wires.app.domain.paging.createPager
import com.wires.app.extensions.toMultipartPart
import com.wires.app.presentation.feed.feedchild.FeedPagingSource
import com.wires.app.presentation.post.CommentsPagingSource
import com.wires.app.presentation.profile.UserPostsPagingSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val apiService: WiresApiService,
    private val postsMapper: PostsMapper,
    private val gson: Gson
) {

    companion object {
        private const val IMAGE_PART_NAME = "image"
    }

    suspend fun getPostsCompilation(interest: UserInterest?, limit: Int, offset: Int): List<Post> {
        delay(2000)
        return apiService.getPostsCompilation(interest?.name, limit, offset).data.map { postsMapper.fromResponseToModel(it) }
    }

    fun getPostsFlow(interest: UserInterest?): Flow<PagingData<Post>> {
        return createPager(FeedPagingSource(this, interest)).flow
    }

    suspend fun createPost(text: String, topic: UserInterest, imagePath: String?) {
        apiService.createPost(
            gson.toJson(PostCreateParams(text, topic.name)).toRequestBody(),
            imagePath?.let { File(it).toMultipartPart(IMAGE_PART_NAME) }
        )
    }

    suspend fun getPost(postId: Int): Post {
        return postsMapper.fromResponseToModel(apiService.getPost(postId).data)
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

    suspend fun getUserPosts(userId: Int, limit: Int, offset: Int): List<Post> {
        return apiService.getUserPosts(userId, limit, offset).data.map(postsMapper::fromResponseToModel)
    }

    fun getUserPostsFlow(userId: Int): Flow<PagingData<Post>> {
        return createPager(UserPostsPagingSource(this, userId)).flow
    }

    suspend fun likePost(postId: Int, isLiked: Boolean) {
        apiService.likePost(postId, isLiked)
    }
}
