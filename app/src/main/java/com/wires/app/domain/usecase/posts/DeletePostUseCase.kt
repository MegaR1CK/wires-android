package com.wires.app.domain.usecase.posts

import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Удаление поста
 */
class DeletePostUseCase @Inject constructor(
    private val postRepository: PostsRepository
) : UseCaseLoadable<DeletePostUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        postRepository.deletePost(params.postId)
    }

    data class Params(
        val postId: Int
    )
}
