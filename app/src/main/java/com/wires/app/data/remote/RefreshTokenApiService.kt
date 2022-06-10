package com.wires.app.data.remote

import com.wires.app.data.remote.params.TokenRefreshParams
import com.wires.app.data.remote.response.ObjectResponse
import com.wires.app.data.remote.response.TokenPairResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApiService {

    @POST("user/refresh")
    suspend fun refreshToken(@Body params: TokenRefreshParams): ObjectResponse<TokenPairResponse>
}
