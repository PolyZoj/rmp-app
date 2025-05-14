package com.ifmo.rmp.data.model

data class RegistrationRequest(
    val username: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val avatar_url: String,
    val weight: Int,
    val height: Int,
    val birth_date: String,
    val unit_system: String = "METRIC",
    val energy_system: String = "KCAL",
    val health_goal: String = "AAA",
    val daily_step_goal: Int,
    val water_intake_goal: Int,
    val calorie_goal: Int,
    val sleep_goal: Float = 8.0f,
    val workouts_goal: Int
)

data class RegistrationResponse(
    val id: String,
    val token: String
)