package com.wires.app.data.remote

import com.wires.app.data.remote.params.CommentAddParams
import com.wires.app.data.remote.params.MessagesReadParams
import com.wires.app.data.remote.params.UserLoginParams
import com.wires.app.data.remote.params.UserPasswordChangeParams
import com.wires.app.data.remote.params.UserRegisterParams
import com.wires.app.data.remote.response.ChannelPreviewResponse
import com.wires.app.data.remote.response.ChannelResponse
import com.wires.app.data.remote.response.CommentResponse
import com.wires.app.data.remote.response.ListResponse
import com.wires.app.data.remote.response.MessageResponse
import com.wires.app.data.remote.response.ObjectResponse
import com.wires.app.data.remote.response.PostResponse
import com.wires.app.data.remote.response.TokenResponse
import com.wires.app.data.remote.response.UserPreviewResponse
import com.wires.app.data.remote.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface WiresApiService {

    @POST("user/register")
    suspend fun registerUser(@Body params: UserRegisterParams): ObjectResponse<Unit>

    @POST("user/login")
    suspend fun loginUser(@Body params: UserLoginParams): ObjectResponse<TokenResponse>

    @GET("user")
    suspend fun getCurrentUser(): ObjectResponse<UserResponse>

    @GET("user/{id}")
    suspend fun getUser(@Path("id") userId: Int): ObjectResponse<UserResponse>

    @Multipart
    @PUT("user/update")
    suspend fun updateUser(
        @Part("update_params") updateParams: RequestBody,
        @Part avatar: MultipartBody.Part?
    ): ObjectResponse<Unit>

    @PUT("user/change_password")
    suspend fun changeUserPassword(@Body params: UserPasswordChangeParams): ObjectResponse<Unit>

    @GET("user/search")
    suspend fun searchUsers(@Query("query") query: String): ListResponse<UserPreviewResponse>

    @GET("posts")
    suspend fun getPostsCompilation(
        @Query("topic") topic: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ListResponse<PostResponse>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") postId: Int): ObjectResponse<PostResponse>

    @GET("posts/{id}/comment")
    suspend fun getPostComments(
        @Path("id") postId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ListResponse<CommentResponse>

    @POST("posts/{id}/comment")
    suspend fun commentPost(@Path("id") postId: Int, @Body params: CommentAddParams): ObjectResponse<Unit>

    @Multipart
    @POST("posts/create")
    suspend fun createPost(
        @Part("create_params") createParams: RequestBody,
        @Part image: MultipartBody.Part?
    ): ObjectResponse<Unit>

    @POST("posts/{id}/like")
    suspend fun likePost(
        @Path("id") postId: Int,
        @Query("is_liked") isLiked: Boolean
    ): ObjectResponse<Unit>

    @Multipart
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: Int,
        @Part("update_params") updateParams: RequestBody,
        @Part image: MultipartBody.Part?
    ): ObjectResponse<Unit>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Int): ObjectResponse<Unit>

    @GET("channels")
    suspend fun getChannels(): ListResponse<ChannelPreviewResponse>

    @GET("channels/{id}")
    suspend fun getChannel(@Path("id") channelId: Int): ObjectResponse<ChannelResponse>

    @GET("channels/{id}/messages")
    suspend fun getChannelMessages(
        @Path("id") channelId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ListResponse<MessageResponse>

    @Multipart
    @POST("channels/create")
    suspend fun createChannel(
        @Part("create_params") createParams: RequestBody,
        @Part image: MultipartBody.Part?
    ): ObjectResponse<ChannelResponse>

    @POST("channels/{id}/read")
    suspend fun readChannelMessages(
        @Path("id") channelId: Int,
        @Body params: MessagesReadParams
    ): ObjectResponse<Unit>

    @GET("user/{id}/posts")
    suspend fun getUserPosts(
        @Path("id") userId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ListResponse<PostResponse>
}
