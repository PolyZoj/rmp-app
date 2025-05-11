package com.ifmo.rmp.data.api

import android.content.Context
import com.ifmo.rmp.data.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val AUTH_BASE_URL = "http://10.0.2.2:8081/"
    private const val USER_BASE_URL = "http://10.0.2.2:8082/"
    private const val CLUB_BASE_URL = "http://10.0.2.2:8085/"
    private const val STATS_BASE_URL = "http://10.0.2.2:8083/"

    private var authApi: AuthApiService? = null
    private var userApi: UserApiService? = null
    private var clubApi: ClubApiService? = null
    private var statsApi: StatsApiService? = null

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun provideRetrofit(baseUrl: String, context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideAuthApiService(context: Context): AuthApiService {
        if (authApi == null) {
            authApi = provideRetrofit(AUTH_BASE_URL, context).create(AuthApiService::class.java)
        }
        return authApi!!
    }

    fun provideUserApiService(context: Context): UserApiService {
        if (userApi == null) {
            userApi = provideRetrofit(USER_BASE_URL, context).create(UserApiService::class.java)
        }
        return userApi!!
    }

    fun provideClubApiService(context: Context): ClubApiService {
        if (clubApi == null) {
            clubApi = provideRetrofit(CLUB_BASE_URL, context).create(ClubApiService::class.java)
        }
        return clubApi!!
    }

    fun provideStatsApiService(context: Context): StatsApiService {
        if (statsApi == null) {
            statsApi = provideRetrofit(STATS_BASE_URL, context).create(StatsApiService::class.java)
        }
        return statsApi!!
    }
}