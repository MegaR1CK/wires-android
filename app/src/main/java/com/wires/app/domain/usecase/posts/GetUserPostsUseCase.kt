package com.wires.app.domain.usecase.posts

import androidx.paging.PagingData
import com.wires.app.data.model.Post
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCasePaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Получение постов пользователя
 */
class GetUserPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCasePaging<GetUserPostsUseCase.Params, Post>() {

    override fun execute(params: Params): Flow<PagingData<Post>> {
        return postsRepository.getUserPostsFlow(params.userId)
    }

    data class Params(
        val userId: Int
    )
}
