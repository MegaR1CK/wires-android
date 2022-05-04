package com.wires.app.domain.usecase.posts

import com.wires.app.data.model.UserInterest
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Обновление поста
 */
class UpdatePostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCaseLoadable<UpdatePostUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        postsRepository.updatePost(params.postId, params.text, params.topic, params.imagePath)
    }

    data class Params(
        val postId: Int,
        val text: String?,
        val topic: UserInterest?,
        val imagePath: String?
    )
}
