package com.wires.app.data.mapper

import com.wires.app.data.model.Comment
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserInterest
import com.wires.app.data.remote.response.CommentResponse
import com.wires.app.data.remote.response.PostResponse
import javax.inject.Inject

class PostsMapper @Inject constructor(
    private val userMapper: UserMapper,
    private val imagesMapper: ImagesMapper
) {

    fun fromResponseToModel(postResponse: PostResponse): Post {
        return Post(
            id = postResponse.id,
            author = userMapper.fromResponseToModel(postResponse.author),
            text = postResponse.text,
            topic = UserInterest.valueOf(postResponse.topic),
            image = postResponse.image?.let { imagesMapper.fromResponseToModel(it) },
            commentsCount = postResponse.commentsCount,
            likesCount = postResponse.likesCount,
            isLiked = postResponse.isUserLiked,
            publishTime = postResponse.publishTime
        )
    }

    fun fromResponseToModel(commentResponse: CommentResponse): Comment {
        return Comment(
            id = commentResponse.id,
            author = userMapper.fromResponseToModel(commentResponse.author),
            text = commentResponse.text,
            sendTime = commentResponse.sendTime
        )
    }
}
