package com.wires.app.domain.usecase.posts

import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Добавление комментария к посту
 */
class CommentPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCaseLoadable<CommentPostUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        postsRepository.commentPost(params.postId, params.text)
    }

    data class Params(
        val postId: Int,
        val text: String
    )
}
