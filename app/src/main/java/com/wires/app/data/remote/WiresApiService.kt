package com.wires.app.data.remote

import com.wires.app.data.remote.params.CommentAddParams
import com.wires.app.data.remote.params.UserLoginParams
import com.wires.app.data.remote.params.UserRegisterParams
import com.wires.app.data.remote.response.ChannelPreviewResponse
import com.wires.app.data.remote.response.CommentResponse
import com.wires.app.data.remote.response.ListResponse
import com.wires.app.data.remote.response.ObjectResponse
import com.wires.app.data.remote.response.PostResponse
import com.wires.app.data.remote.response.TokenResponse
import com.wires.app.data.remote.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
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
        @Part avatar: MultipartBody.Part?
    ): ObjectResponse<Unit>

    @GET("channels")
    suspend fun getChannels(): ListResponse<ChannelPreviewResponse>
}
