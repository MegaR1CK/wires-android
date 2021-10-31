package com.wires.app.di.module

import android.content.Context
import android.text.TextUtils
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.wires.app.data.remote.WiresApiService
import com.wires.app.domain.repository.TokenRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class ApiServiceModule {

    companion object {
        private const val HEADER_AUTH = "Authorization"
        private const val BASE_URL = "test"
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
                if (!TextUtils.isEmpty(token)) {
                    requestBuilder
                        .header(HEADER_AUTH, token)
                        .method(original.method, original.body)
                }
                chain.proceed(requestBuilder.build())
            }
            addCommonInterceptors(context)
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiService(client: OkHttpClient): WiresApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WiresApiService::class.java)

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
