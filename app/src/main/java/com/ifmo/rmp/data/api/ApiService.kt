package com.ifmo.rmp.data.api

import com.ifmo.rmp.data.model.ClubCreateRequest
import com.ifmo.rmp.data.model.ClubCreateResponse
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.model.ClubsListResponse
import com.ifmo.rmp.data.model.DailyStatsResponse
import com.ifmo.rmp.data.model.FindFriendListResponse
import com.ifmo.rmp.data.model.FindFriendsRequest
import com.ifmo.rmp.data.model.FriendListResponse
import com.ifmo.rmp.data.model.FriendOptionsRequest
import com.ifmo.rmp.data.model.FriendOptionsResponse
import com.ifmo.rmp.data.model.FriendRequestsListResponse
import com.ifmo.rmp.data.model.IdByUsernameResponse
import com.ifmo.rmp.data.model.LoginRequest
import com.ifmo.rmp.data.model.LoginResponse
import com.ifmo.rmp.data.model.Profile
import com.ifmo.rmp.data.model.RegistrationRequest
import com.ifmo.rmp.data.model.RegistrationResponse
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.model.UserUpdateRequest
import com.ifmo.rmp.data.model.Achievement
import com.ifmo.rmp.data.model.Achievements

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegistrationRequest): RegistrationResponse
}

interface ClubApiService {
    @GET("api/v1/clubs/{clubId}")
    suspend fun getClubInfo(@Path("clubId") clubId: String): ClubInfoResponse

    @GET("api/v1/clubs/list")
    suspend fun getClubsList(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): ClubsListResponse

    @POST("api/v1/clubs/create")
    suspend fun createClub(@Body request: ClubCreateRequest): ClubCreateResponse
}

interface UserApiService {

    @GET("api/v1/users/friends/notifications")
    suspend fun getNotifications(): FriendRequestsListResponse

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

    @PUT("api/v1/users/goals/update")
    suspend fun updateUserProfile(@Body request: UserUpdateRequest): FriendOptionsResponse

    @GET("api/v1/users/friends/list")
    suspend fun getFriendsList(): FriendListResponse

    @POST("api/v1/users/friends/find")
    suspend fun findFriends(@Body request: FindFriendsRequest): FindFriendListResponse

}

interface StatsApiService {

    @GET("api/v1/stats/daily/{user_id}/{date}")
    suspend fun getDailyStats(@Path("user_id") userId: String, @Path("date") date: String): DailyStatsResponse

}

interface ChallengesApiService {

    @GET("/api/v1/challenges/achievements/{id}")
    suspend fun getChallengesById(@Path("id") id: String): Achievements

    @GET("/api/v1/challenges/achievements/{id}/{day}")
    suspend fun getChallengesByIdByDay(@Path("id") id: String, @Path("day") day: String): Achievements

    @GET("/api/v1/challenges/achievements/{id}/today")
    suspend fun getChallengesByIdByToday(@Path("id") id: String): Achievements

}