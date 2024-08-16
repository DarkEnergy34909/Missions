package com.example.missions.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class MissionRepository(context: Context) {
    private val missionDao = MissionDatabase.getDatabase(context).missionDao()

    suspend fun insertMission(mission: Mission) {
        missionDao.insert(mission)
    }

    suspend fun updateMission(mission: Mission) {
        missionDao.update(mission)
    }

    suspend fun deleteMission(mission: Mission) {
        missionDao.delete(mission)
    }

    suspend fun getMission(text: String): Mission {
        return missionDao.getMission(text)
    }

    suspend fun getAllMissions(): List<Mission> {
        return missionDao.getAllMissions()
    }

    suspend fun getUncompletedMissions(): List<Mission> {
        return missionDao.getUncompletedMissions()
    }

    suspend fun getCompletedMissions(): List<Mission> {
        return missionDao.getCompletedMissions()
    }
}