package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.api.StatsApiService
import com.ifmo.rmp.data.model.DailyStatsResponse

class StatsRepository(private val statsApiService: StatsApiService) {

    companion object {
        @Volatile
        private var instance: StatsRepository? = null

        fun getInstance(context: Context): StatsRepository {
            return instance ?: synchronized(this) {
                instance ?: StatsRepository(
                    NetworkModule.provideStatsApiService(context)
                ).also { instance = it }
            }
        }
    }

    suspend fun getDailyStats(userId: String, date: String): Result<DailyStatsResponse> {
        return try {
            val response = statsApiService.getDailyStats(userId, date)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}