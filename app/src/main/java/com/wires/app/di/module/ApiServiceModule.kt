package com.wires.app.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.neovisionaries.ws.client.WebSocketFactory
import com.wires.app.data.remote.RefreshTokenApiService
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.typeadapters.LocalDateTimeAdapter
import com.wires.app.domain.usecase.auth.GetAccessTokenUseCase
import com.wires.app.domain.usecase.auth.GetRefreshTokenUseCase
import com.wires.app.domain.usecase.auth.RefreshTokenUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.HttpURLConnection
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class ApiServiceModule {

    companion object {
        private const val HEADER_AUTH = "Authorization"
        private const val AUTH_TOKEN_PREFIX = "Bearer"
        private const val BASE_URL = "https://wires-api.herokuapp.com/v1/"
        private const val CONNECTION_TIMEOUTS_MS = 20000L
    }

    /**
     * В authenticator OkHttpClient нужно рефрешнуть токен. Для этого нужен репозиторий, который вызовет запрос.
     * Но если используется один OkHttpClient, получим циклическую зависимость.
     * Так что создаем дополнительный OkHttpClient, apiService и репозиторий
     */
    @Qualifier
    annotation class ApiClient

    @Qualifier
    annotation class RefreshTokenClient

    @ApiClient
    @Provides
    @Singleton
    fun provideOkHttpClient(
        context: Context,
        getAccessTokenUseCase: GetAccessTokenUseCase,
        getRefreshTokenUseCase: GetRefreshTokenUseCase,
        refreshTokenUseCase: RefreshTokenUseCase
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            setTimeouts()
            authenticator(getAuthenticator(refreshTokenUseCase, getRefreshTokenUseCase, getAccessTokenUseCase))
            addInterceptor { chain ->
                val original = chain.request()
                val accessToken = getAccessTokenUseCase.execute(Unit).orEmpty()
                val requestBuilder = original.newBuilder()
                Timber.tag("OkHttp").d("Auth-Token: %s", accessToken)
                if (accessToken.isNotEmpty()) {
                    requestBuilder
                        .header(HEADER_AUTH, "$AUTH_TOKEN_PREFIX $accessToken")
                        .method(original.method, original.body)
                }
                chain.proceed(requestBuilder.build())
            }
            addCommonInterceptors(context)
        }.build()
    }

    @RefreshTokenClient
    @Provides
    @Singleton
    fun provideRefreshClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder().apply {
            setTimeouts()
            addCommonInterceptors(context)
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiService(@ApiClient client: OkHttpClient, gson: Gson): WiresApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WiresApiService::class.java)

    @Provides
    @Singleton
    fun provideRefreshApiService(@RefreshTokenClient client: OkHttpClient, gson: Gson): RefreshTokenApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RefreshTokenApiService::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
    }

    @Provides
    @Singleton
    fun provideWebSocketFactory() = WebSocketFactory()

    private fun OkHttpClient.Builder.setTimeouts() {
        connectTimeout(CONNECTION_TIMEOUTS_MS, TimeUnit.MILLISECONDS)
        readTimeout(CONNECTION_TIMEOUTS_MS, TimeUnit.MILLISECONDS)
        writeTimeout(CONNECTION_TIMEOUTS_MS, TimeUnit.MILLISECONDS)
    }

    private fun OkHttpClient.Builder.addCommonInterceptors(context: Context) {
        addInterceptor(
            ChuckerInterceptor.Builder(context)
                .alwaysReadResponseBody(true)
                .build()
        )
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

    private fun getAuthenticator(
        refreshTokenUseCase: RefreshTokenUseCase,
        getRefreshTokenUseCase: GetRefreshTokenUseCase,
        getAccessTokenUseCase: GetAccessTokenUseCase
    ) = object : Authenticator {

        val mutex = Mutex()

        override fun authenticate(route: Route?, response: Response): Request? {
            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val oldToken = getAccessTokenUseCase.execute(Unit)
                val newToken = runBlocking {
                    mutex.withLock {
                        val currentToken = getAccessTokenUseCase.execute(Unit)
                        return@runBlocking when {
                            currentToken == null -> null
                            currentToken != oldToken -> currentToken
                            !getRefreshTokenUseCase.execute(Unit).isNullOrEmpty() -> {
                                try {
                                    refreshTokenUseCase.execute(Unit).accessToken
                                } catch (throwable: Throwable) {
                                    null
                                }
                            }
                            else -> null
                        }
                    }
                }
                newToken?.let { token ->
                    return response.request
                        .newBuilder()
                        .header(HEADER_AUTH, "$AUTH_TOKEN_PREFIX $token")
                        .build()
                }
            }
            return null
        }
    }
}
