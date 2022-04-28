package com.wires.app.domain.usecase.posts

import com.wires.app.data.model.SetLikeResult
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.extensions.parseError
import javax.inject.Inject

/**
 * Установка/снятие лайка на посте
 * В случае ошибки также возвращаем [SetLikeResult], но с указанной ошибкой
 */
class LikePostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCaseLoadable<LikePostUseCase.Params, SetLikeResult>() {

    override suspend fun execute(params: Params): SetLikeResult {
        return try {
            postsRepository.likePost(params.postId, params.isLiked)
            SetLikeResult(params.postId, error = null)
        } catch (throwable: Throwable) {
            SetLikeResult(params.postId, throwable.parseError())
        }
    }

    data class Params(
        val postId: Int,
        val isLiked: Boolean
    )
}
