package com.wires.app.presentation.profile

import com.wires.app.data.model.Post
import com.wires.app.domain.paging.BasePagingSource
import com.wires.app.domain.repository.PostsRepository

class UserPostsPagingSource(
    private val postsRepository: PostsRepository,
    private val userId: Int
) : BasePagingSource<Post>() {
    override suspend fun loadPage(offset: Int, limit: Int): List<Post> {
        return postsRepository.getUserPosts(userId, limit, offset)
    }
}
