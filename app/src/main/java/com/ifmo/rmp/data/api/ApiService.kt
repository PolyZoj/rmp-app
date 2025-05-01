package com.ifmo.rmp.data.api

import com.ifmo.rmp.data.model.LoginRequest
import com.ifmo.rmp.data.model.LoginResponse
import com.ifmo.rmp.data.model.Profile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/v1/users/{userId}/profile")
    suspend fun getUserProfile(@Path("userId") userId: String): Profile
} 