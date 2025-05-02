package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.ApiService
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.model.ClubCreateRequest
import com.ifmo.rmp.data.model.ClubCreateResponse
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.model.ClubsListResponse

class ClubRepository(private val apiService: ApiService) {
    
    companion object {
        @Volatile
        private var instance: ClubRepository? = null
        
        fun getInstance(context: Context): ClubRepository {
            return instance ?: synchronized(this) {
                instance ?: ClubRepository(
                    NetworkModule.provideApiService(context)
                ).also { instance = it }
            }
        }
    }
    
    suspend fun getClubInfo(clubId: String): Result<ClubInfoResponse> {
        return try {
            val clubInfo = apiService.getClubInfo(clubId)
            Result.success(clubInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getClubsList(limit: Int = 10, offset: Int = 0): Result<ClubsListResponse> {
        return try {
            val response = apiService.getClubsList(limit, offset)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createClub(name: String, description: String): Result<ClubCreateResponse> {
        return try {
            val request = ClubCreateRequest(name = name, description = description)
            val response = apiService.createClub(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 