package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.ChallengesApiService
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.model.Achievement

class ChallengesRepository(private val challengesApiService: ChallengesApiService) {

    companion object {
        @Volatile
        private var instance: ChallengesRepository? = null

        fun getInstance(context: Context): ChallengesRepository {
            return instance ?: synchronized(this) {
                instance ?: ChallengesRepository(
                    NetworkModule.provideChallengesApiService(context)
                ).also { instance = it }
            }
        }
    }

    suspend fun getAchievementsById(id: String): Result<ArrayList<Achievement>> {
        return try {
            val response = challengesApiService.getChallengesById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAchievementsByIdByDay(id: String, day: String): Result<ArrayList<Achievement>> {
        return try {
            val response = challengesApiService.getChallengesByIdByDay(id, day)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAchievementsByIdByToday(id: String): Result<ArrayList<Achievement>> {
        return try {
            val response = challengesApiService.getChallengesByIdByToday(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}