package com.example.missions.data

import kotlinx.coroutines.flow.Flow

interface MissionsRepository {
    fun getAllMissionsStream(): Flow<List<Mission>>

    fun getMissionStream(id: Int): Flow<Mission>

    suspend fun insertMission(mission: Mission)

    suspend fun deleteMission(mission: Mission)

    suspend fun updateMission(mission: Mission)
}