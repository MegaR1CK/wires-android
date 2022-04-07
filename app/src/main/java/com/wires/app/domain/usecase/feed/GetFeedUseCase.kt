package com.wires.app.domain.usecase.feed

import androidx.paging.PagingData
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCasePaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Получение списка постов по тематике
 */
class GetFeedUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCasePaging<GetFeedUseCase.Params, Post>() {

    override fun execute(params: Params): Flow<PagingData<Post>> {
        return postsRepository.getPostsFlow(params.interest)
    }

    data class Params(
        val interest: UserInterest?
    )
}
