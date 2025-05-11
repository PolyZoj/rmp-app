package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.api.UserApiService
import com.ifmo.rmp.data.model.*

class UserRepository(private val userApiService: UserApiService) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(
                    NetworkModule.provideUserApiService(context)
                ).also { instance = it }
            }
        }
    }

    suspend fun getNotificationList(): Result<FriendRequestsListResponse>{
        return try {
            Result.success(userApiService.getNotifications())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptFriendRequest(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(userApiService.acceptRequest(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun denyFriendRequest(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(userApiService.denyRequest(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFriend(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(userApiService.addFriend(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFriend(friendId: Int): Result<FriendOptionsResponse> {
        return try {
            val request = FriendOptionsRequest(friend_id = friendId)
            Result.success(userApiService.removeFriend(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserData(userId: String): Result<UserDtoResponse> {
        return try {
            Result.success(userApiService.userData(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserIdByUsername(username: String): Result<IdByUsernameResponse> {
        return try {
            Result.success(userApiService.getIdByUsername(username))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(request: UserUpdateRequest): Result<FriendOptionsResponse> {
        return try {
            Result.success(userApiService.updateUserProfile(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFriendsList(): Result<FriendListResponse> {
        return try {
            Result.success(userApiService.getFriendsList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun findFriends(username: String): Result<FindFriendListResponse> {
        return try {
            Result.success(userApiService.findFriends(FindFriendsRequest(username)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
