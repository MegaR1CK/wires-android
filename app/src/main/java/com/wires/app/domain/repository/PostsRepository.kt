package com.wires.app.domain.repository

import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.managers.MockManager
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val mockManager: MockManager
) {
    suspend fun getPosts(interests: List<UserInterest>): List<Post> {
        return mockManager.getPosts(interests)
    }
}
