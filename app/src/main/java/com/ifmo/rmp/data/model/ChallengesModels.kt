package com.ifmo.rmp.data.model

import com.ifmo.rmp.R
import java.time.LocalDate

enum class AchievementStatus {
    IN_PROGRESS,
    COMPLETED
}

enum class AchievementType {
    level,
    xp,
    steps_count,
    calorie_count,
    water_count,
    workouts_count
}

data class Achievement(
    val id: String? = null,
    val userId: String,
    val icon: String,
    val description: String,
    val title: String,
    val status: AchievementStatus,
    val goal: Double,
    val type: AchievementType,
    val start_date: LocalDate,
    val end_date: LocalDate
)

fun getDrawableResId(iconName: String): Int {
    return when (iconName) {
        "e_step" -> R.drawable.e_step
        "e_water" -> R.drawable.e_water
        "e_workout" -> R.drawable.e_workout
        else -> R.drawable.e_trophy
    }
}
