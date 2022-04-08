package com.wires.app.presentation.post

import com.wires.app.data.model.Comment
import com.wires.app.domain.paging.BasePagingSource
import com.wires.app.domain.repository.PostsRepository

class CommentsPagingSource(
    private val postsRepository: PostsRepository,
    private val postId: Int
) : BasePagingSource<Comment>() {
    override suspend fun loadPage(offset: Int, limit: Int): List<Comment> {
        return postsRepository.getComments(postId, limit, offset)
    }
}
