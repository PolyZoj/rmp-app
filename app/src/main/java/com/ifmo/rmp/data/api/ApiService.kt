package com.ifmo.rmp.data.api

import com.ifmo.rmp.data.model.ClubCreateRequest
import com.ifmo.rmp.data.model.ClubCreateResponse
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.model.ClubsListResponse
import com.ifmo.rmp.data.model.LoginRequest
import com.ifmo.rmp.data.model.LoginResponse
import com.ifmo.rmp.data.model.Profile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/v1/users/{userId}/profile")
    suspend fun getUserProfile(@Path("userId") userId: String): Profile
    
    @GET("api/v1/clubs/{clubId}")
    suspend fun getClubInfo(@Path("clubId") clubId: String): ClubInfoResponse
    
    @GET("api/v1/clubs/list")
    suspend fun getClubsList(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): ClubsListResponse
    
    @POST("api/v1/clubs/create")
    suspend fun createClub(@Body request: ClubCreateRequest): ClubCreateResponse
} 