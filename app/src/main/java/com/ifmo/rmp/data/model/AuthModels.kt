package com.ifmo.rmp.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val id: String,
    val token: String
) 