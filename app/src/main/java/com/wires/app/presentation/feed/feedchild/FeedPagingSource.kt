package com.wires.app.presentation.feed.feedchild

import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.paging.BasePagingSource
import com.wires.app.domain.repository.PostsRepository

class FeedPagingSource(
    private val postsRepository: PostsRepository,
    private val interest: UserInterest?
) : BasePagingSource<Post>() {
    override suspend fun loadPage(offset: Int, limit: Int): List<Post> {
        return postsRepository.getPostsCompilation(interest, limit, offset)
    }
}
