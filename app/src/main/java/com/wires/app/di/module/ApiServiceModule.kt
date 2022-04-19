package com.wires.app.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.neovisionaries.ws.client.WebSocketFactory
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.typeadapters.LocalDateTimeAdapter
import com.wires.app.domain.repository.TokenRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class ApiServiceModule {

    companion object {
        private const val HEADER_AUTH = "Authorization"
        private const val AUTH_TOKEN_PREFIX = "Bearer"
        private const val BASE_URL = "https://wires-api.herokuapp.com/v1/"
        private const val CONNECTION_TIMEOUTS_MS = 20000L
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, tokenRepository: TokenRepository): OkHttpClient {
        return OkHttpClient.Builder().apply {
            setTimeouts()
            addInterceptor { chain ->
                val original = chain.request()
                val token = tokenRepository.getAccessToken() ?: ""
                val requestBuilder = original.newBuilder()
                Timber.tag("OkHttp").d("Auth-Token: %s", token)
                if (token.isNotEmpty()) {
                    requestBuilder
                        .header(HEADER_AUTH, "$AUTH_TOKEN_PREFIX $token")
                        .method(original.method, original.body)
                }
                chain.proceed(requestBuilder.build())
            }
            addCommonInterceptors(context)
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiService(client: OkHttpClient, gson: Gson): WiresApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WiresApiService::class.java)

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
}
