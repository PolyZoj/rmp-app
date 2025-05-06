package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.ApiService
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.model.*

class UserRepository(private val apiService: ApiService) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(
                    NetworkModule.provideApiService(context)
                ).also { instance = it }
            }
        }
    }

    suspend fun getUserProfile(userId: String, token: String): Result<Profile> {
        return try {
            val profile = apiService.getUserProfile(userId, "Bearer $token")
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptFriendRequest(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(apiService.acceptRequest(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun denyFriendRequest(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(apiService.denyRequest(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFriend(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(apiService.addFriend(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFriend(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(apiService.removeFriend(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserData(userId: String): Result<UserDtoResponse> {
        return try {
            Result.success(apiService.userData(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserIdByUsername(username: String): Result<IdByUsernameResponse> {
        return try {
            Result.success(apiService.getIdByUsername(username))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(request: UserUpdateRequest): Result<FriendOptionsResponse> {
        return try {
            Result.success(apiService.updateUserProfile(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFriendsList(): Result<FriendListResponse> {
        return try {
            Result.success(apiService.getFriendsList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun findFriends(username: String): Result<FindFriendListResponse> {
        return try {
            Result.success(apiService.findFriends(FindFriendsRequest(username)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
