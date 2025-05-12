package com.ifmo.rmp.data.model

import com.ifmo.rmp.R
import java.time.LocalDate

data class Achievement(
    val id: String? = null,
    val userId: String,
    val icon: String,
    val description: String,
    val title: String,
    val status: String,
    val goal: Double,
    val type: String,
    val start_date: String,
    val end_date: String
)

data class Achievements(
    val achievements: List<Achievement>
)

fun getDrawableResId(iconName: String): Int {
    return when (iconName) {
        "e_step" -> R.drawable.e_step
        "e_water" -> R.drawable.e_water
        "e_workout" -> R.drawable.e_workout
        else -> R.drawable.e_trophy
    }
}
