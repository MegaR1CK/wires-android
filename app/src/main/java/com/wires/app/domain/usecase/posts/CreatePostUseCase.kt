package com.wires.app.domain.usecase.posts

import com.wires.app.data.model.UserInterest
import com.wires.app.domain.repository.PostsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) : UseCaseLoadable<CreatePostUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        postsRepository.createPost(params.text, params.topic, params.imagePath)
    }

    data class Params(
        val text: String,
        val topic: UserInterest,
        val imagePath: String?
    )
}
