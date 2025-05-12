package com.ifmo.rmp.data.model

data class DailyStatsResponse(
    val date: String,
    val level: Int,
    val xp: Int,
    val calorie_count: Int,
    val water_count: Int,
    val workouts_count: Int,
    val completed_challenges: Int
)

data class AddStatsRequest(
    val id: String,
    val type: String,
    val add: Int
)

data class AddStatsResponse(
    val status: Boolean
)