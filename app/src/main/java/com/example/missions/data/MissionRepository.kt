package com.example.missions.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

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

    suspend fun getMission(id: Int): Mission {
        if (id == -1) {
            return Mission(id = -1, text = "No challenges available", difficulty = "Easy", completed = false, dateCompleted = "")
        }
        return missionDao.getMission(id)
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

suspend fun getNewMission(repository: MissionRepository): Mission {
    val missionList = repository.getUncompletedMissions()
    if (missionList.isEmpty()) {
        return Mission(id = -1, text = "No challenges available", difficulty = "Easy", completed = false, dateCompleted = "")
    }
    return missionList[Random.nextInt(0, missionList.size)]
}