package com.wires.app.managers

import com.wires.app.data.model.Channel
import com.wires.app.data.model.Comment
import com.wires.app.data.model.Image
import com.wires.app.data.model.ImageSize
import com.wires.app.data.model.Message
import com.wires.app.data.model.Post
import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import com.wires.app.data.model.UserPreview
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
            avatar = Image("https://placekitten.com/50/50", ImageSize(50, 50)),
            interests = listOf(UserInterest.ANDROID_DEVELOPMENT)
        )
    }

    suspend fun getPosts(interests: List<UserInterest>): List<Post> {
        return List(10) {
            Post(
                id = 1,
                author = UserPreview(1, "name", null, null, null),
                publishTime = LocalDateTime.parse("2021-11-03T10:15:30"),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris, ac" +
                    " tincidunt risus pellentesque id. Curabitur tincidunt enim sed eros elementum, vel pretium nisl congue.",
                image = Image(
                    "https://4.img-dpreview.com/files/p/TC1200x630S1200x630~sample_galleries/1330372094/1693761761.jpg",
                    ImageSize(1200, 630)
                ),
                likesCount = 100,
                commentsCount = 150,
                isLiked = false,
                topic = "android_dev"
            )
        }
    }

    suspend fun getPost(postId: Int): Post {
        return Post(
            id = 1,
            author = UserPreview(1, "name", null, null, null),
            publishTime = LocalDateTime.parse("2021-11-03T10:15:30"),
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris, ac" +
                " tincidunt risus pellentesque id. Curabitur tincidunt enim sed eros elementum, vel pretium nisl congue.",
            image = Image(
                "https://4.img-dpreview.com/files/p/TC1200x630S1200x630~sample_galleries/1330372094/1693761761.jpg",
                ImageSize(1200, 630)
            ),
            likesCount = 100,
            commentsCount = 150,
            isLiked = false,
            topic = "android_dev"
        )
    }

    suspend fun getComments(postId: Int): List<Comment> {
        return List(10) {
            Comment(
                id = 1,
                author = UserPreview(1, "name", null, null, null),
                sendTime = LocalDateTime.parse("2021-11-03T10:15:30"),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris, ac" +
                    " tincidunt risus pellentesque id. Curabitur tincidunt enim sed eros elementum, vel pretium nisl congue."
            )
        }
    }

    suspend fun addComment(comment: Comment) {
        delay(1000)
    }

    suspend fun getChannels(): List<Channel> {
        return List(3) {
            Channel(
                id = 1,
                name = "ChannelName",
                members = List(3) { UserPreview(1, "name", null, null, null) },
                image = Image("https://placekitten.com/70/70", ImageSize(70, 70)),
            )
        }
    }

    suspend fun getMessages(channelId: Int): List<Message> {
        delay(1000)
        return List(10) {
            Message(
                id = 1,
                author = UserPreview(1, "name", null, null, null),
                sendTime = LocalDateTime.parse("2021-11-03T10:15:30"),
                messageText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dignissim felis mauris.",
                isInitial = false
            )
        }
    }

    suspend fun sendMessage(channelId: Int, message: Message) {
        delay(1000)
    }
}
