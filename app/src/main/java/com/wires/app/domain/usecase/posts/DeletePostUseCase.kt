package com.wires.app.domain.usecase.posts

import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Удаление поста. Возвращаем id поста для обновления его в списке.
 */
class DeletePostUseCase @Inject constructor(
    private val postRepository: PostsRepository
) : UseCaseLoadable<DeletePostUseCase.Params, Int>() {

    override suspend fun execute(params: Params): Int {
        postRepository.deletePost(params.postId)
        return params.postId
    }

    data class Params(
        val postId: Int
    )
}
