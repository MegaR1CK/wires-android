package com.wires.app.managers

import com.wires.app.data.model.Comment
import com.wires.app.data.model.Post
import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class MockManager @Inject constructor() {

    suspend fun loginUser(email: String, passwordHash: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        delay(1000)
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    suspend fun registerUser(email: String, passwordHash: String, username: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        delay(1000)
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    suspend fun getStoredUser(): User {
        delay(1000)
        return User(
            id = 1,
            username = "kgoncharov",
            firstName = "Konstantin",
            lastName = "Goncharov",
            email = "test@test.ru",
            avatarUrl = "https://placekitten.com/70/70",
            interests = listOf(UserInterest.ANDROID_DEVELOPMENT)
        )
    }

    suspend fun getPosts(interests: List<UserInterest>): List<Post> {
        return List(10) {
            Post(
                id = 1,
                author = getStoredUser(),
                publishTime = LocalDateTime.parse("2021-11-03T10:15:30"),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris, ac" +
                    " tincidunt risus pellentesque id. Curabitur tincidunt enim sed eros elementum, vel pretium nisl congue.",
                imageUrl = "https://4.img-dpreview.com/files/p/TC1200x630S1200x630~sample_galleries/1330372094/1693761761.jpg",
                likesCount = 100,
                commentsCount = 150,
                isLiked = false
            )
        }
    }

    suspend fun getPost(postId: Int): Post {
        return Post(
            id = 1,
            author = getStoredUser(),
            publishTime = LocalDateTime.parse("2021-11-03T10:15:30"),
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris, ac" +
                " tincidunt risus pellentesque id. Curabitur tincidunt enim sed eros elementum, vel pretium nisl congue.",
            imageUrl = "https://4.img-dpreview.com/files/p/TC1200x630S1200x630~sample_galleries/1330372094/1693761761.jpg",
            likesCount = 100,
            commentsCount = 150,
            isLiked = false
        )
    }

    suspend fun getComments(postId: Int): List<Comment> {
        return List(10) {
            Comment(
                id = 1,
                author = getStoredUser(),
                publishTime = LocalDateTime.parse("2021-11-03T10:15:30"),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris, ac" +
                    " tincidunt risus pellentesque id. Curabitur tincidunt enim sed eros elementum, vel pretium nisl congue."
            )
        }
    }

    suspend fun addComment(comment: Comment) {
        delay(1000)
    }
}
