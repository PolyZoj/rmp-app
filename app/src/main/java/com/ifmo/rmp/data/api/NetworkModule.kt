package com.ifmo.rmp.data.api

import android.content.Context
import com.ifmo.rmp.data.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:8085/"
    
    private var apiService: ApiService? = null
    
    fun provideApiService(context: Context): ApiService {
        if (apiService == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
                
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                
            apiService = retrofit.create(ApiService::class.java)
        }
        
        return apiService!!
    }
} 