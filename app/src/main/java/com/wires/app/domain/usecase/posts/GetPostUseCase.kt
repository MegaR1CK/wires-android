package com.wires.app.domain.usecase.posts

import com.wires.app.data.model.Post
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение данных о посте
 */
class GetPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCaseLoadable<GetPostUseCase.Params, Post>() {

    override suspend fun execute(params: Params): Post {
        return postsRepository.getPost(params.postId)
    }

    data class Params(
        val postId: Int
    )
}
