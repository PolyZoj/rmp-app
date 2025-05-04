package com.ifmo.rmp.data.model

data class Profile(
    val id: String,
    val username: String,
    val fullName: String,
    val email: String,
    val bio: String
)

data class FriendStructure(
    val user_id: String,
    val username: String,
    val avatar_url: String,
)

data class FriendRequestsListResponse(
    val friend_requests: List<FriendStructure>
)

data class FriendListResponse(
    val friends: List<FriendStructure>
)

data class FindFriendListResponse(
    val possible_friends: List<FriendStructure>
)

data class FriendOptionsRequest(
    val friend_id: Int
)

data class FindFriendsRequest(
    val find_username: Int
)

data class FriendOptionsResponse(
    val success: String
)

data class IdByUsernameResponse(
    val user_id: String
)

data class UserDtoResponse(
    val user_dto: UserDto
)

data class UserDto(
    val user: User,
    val username: String,
    val weight: Float,
    val height: Int,
    val birth_date: String,
    val unit_system: String,
    val energy_system: String,
    val health_goal: String,
    val daily_step_goal: Int,
    val water_intake_goal: Int,
    val calorie_goal: Int,
    val sleep_goal: Float,
    val workouts_goal: Int
)

data class User(
    val user_id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val avatar_url: String,
    val is_admin: Boolean,
    val club_id: Int?,
    val created_at: String
)

data class UserUpdateRequest(
    val avatar_url: String? = null,
    val weight: Float? = null,
    val height: Int? = null,
    val health_goal: String? = null,
    val daily_step_goal: Int? = null,
    val water_intake_goal: Int? = null,
    val calorie_goal: Int? = null,
    val sleep_goal: Float? = null,
    val workouts_goal: Int? = null
)
