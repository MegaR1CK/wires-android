package com.wires.app.data.mapper

import com.wires.app.data.model.Post
import com.wires.app.data.remote.response.PostResponse
import javax.inject.Inject

class PostsMapper @Inject constructor(
    private val userMapper: UserMapper
) {
    fun fromResponseToModel(postResponse: PostResponse): Post {
        return Post(
            id = postResponse.id,
            author = userMapper.fromResponseToModel(postResponse.author),
            text = postResponse.text,
            topic = postResponse.topic,
            imageUrl = postResponse.imageUrl,
            commentsCount = postResponse.commentsCount,
            likesCount = postResponse.likesCount,
            isLiked = postResponse.isUserLiked,
            publishTime = postResponse.publishTime
        )
    }
}
