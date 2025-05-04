package com.ifmo.rmp.data.api

import com.ifmo.rmp.data.model.ClubCreateRequest
import com.ifmo.rmp.data.model.ClubCreateResponse
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.model.ClubsListResponse
import com.ifmo.rmp.data.model.FriendOptionsRequest
import com.ifmo.rmp.data.model.FriendOptionsResponse
import com.ifmo.rmp.data.model.FriendRequestsListResponse
import com.ifmo.rmp.data.model.IdByUsernameResponse
import com.ifmo.rmp.data.model.LoginRequest
import com.ifmo.rmp.data.model.LoginResponse
import com.ifmo.rmp.data.model.Profile
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.model.UserUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // теперь используем api/v1/users/{userId}, этот запрос потом надо удалить
    @GET("api/v1/users/{userId}/profile")
    suspend fun getUserProfile(@Path("userId") userId: String): Profile
    
    @GET("api/v1/clubs/{clubId}")
    suspend fun getClubInfo(@Path("clubId") clubId: String): ClubInfoResponse
    
    @GET("api/v1/clubs/list")
    suspend fun getClubsList(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): ClubsListResponse
    
    @POST("api/v1/clubs/create")
    suspend fun createClub(@Body request: ClubCreateRequest): ClubCreateResponse

    @GET("/api/v1/users/friends/notifications")
    suspend fun getFriendsNotifications(): FriendRequestsListResponse

    @POST("api/v1/users/friends/notifications/accept")
    suspend fun acceptRequest(@Body request: FriendOptionsRequest): FriendOptionsResponse

    @POST("api/v1/users/friends/notifications/deny")
    suspend fun denyRequest(@Body request: FriendOptionsRequest): FriendOptionsResponse

    @POST("api/v1/users/friends/request")
    suspend fun addFriend(@Body request: FriendOptionsRequest): FriendOptionsResponse

    @POST("api/v1/users/friends/remove")
    suspend fun removeFriend(@Body request: FriendOptionsRequest): FriendOptionsResponse

    @GET("api/v1/users/{userId}")
    suspend fun userData(@Path("userId") userId: String): UserDtoResponse

    @GET("api/v1/users/username/{userName}")
    suspend fun getIdByUsername(@Path("userName") userName: String): IdByUsernameResponse

    @POST("api/v1/users/goals/update")
    suspend fun updateUserProfile(@Body request: UserUpdateRequest): FriendOptionsResponse

}