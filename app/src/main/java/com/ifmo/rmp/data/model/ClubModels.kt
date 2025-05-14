package com.ifmo.rmp.data.model

data class ClubInfoResponse(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val members: List<String> = emptyList(),
    val location: String = "",
    val memberCount: Int = 0,
    val isJoined: Boolean = false
)

data class ClubResponse(
    val club: ClubInfoResponse
)

data class ClubsListResponse(
    val data: List<ClubInfoResponse>
)

data class ClubCreateRequest(
    val name: String,
    val description: String,
    val ownerId: String = "550e8400-e29b-41d4-a716-446655440000"
)

data class ClubCreateResponse(
    val clubId: String,
    val name: String
)

data class ClubMemberRequest(
    val userId: String
)

data class ClubMemberResponse(
    val message: String,
    val userId: String,
    val clubId: String
)

data class ErrorResponse(
    val errorCode: String,
    val message: String
) 