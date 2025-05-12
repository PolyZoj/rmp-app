package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.api.StatsApiService
import com.ifmo.rmp.data.model.AddStatsRequest
import com.ifmo.rmp.data.model.AddStatsResponse
import com.ifmo.rmp.data.model.StatsResponse

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

    suspend fun getStats(userId: String): Result<StatsResponse> {
        return try {
            val response = statsApiService.getStats(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addStats(request: AddStatsRequest): Result<AddStatsResponse> {
        return try {
            val response = statsApiService.addStats(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}