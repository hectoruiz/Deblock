package com.hectoruiz.data.api.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class ApiClient {
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS).build()

    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private companion object {
        const val BASE_URL = "https://api.coingecko.com/"
        const val TIMEOUT = 20L
    }
}
