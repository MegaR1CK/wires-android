package com.wires.app.domain.usecase.posts

import androidx.paging.PagingData
import com.wires.app.data.model.Comment
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCasePaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Получение комментариев поста (с пагинацией)
 */
class GetPostCommentsUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCasePaging<GetPostCommentsUseCase.Params, Comment>() {

    override fun execute(params: Params): Flow<PagingData<Comment>> {
        return postsRepository.getCommentsFlow(params.postId)
    }

    data class Params(
        val postId: Int
    )
}
