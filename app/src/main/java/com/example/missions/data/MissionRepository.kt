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
        if (id == 0) {
            return Mission(id = 0, text = "No challenges available", difficulty = "Easy", completed = false, dateCompleted = "")
        }
        return missionDao.getMission(id)
    }

    suspend fun isEmpty(): Boolean {
        return missionDao.getAllMissions().isEmpty()
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

    suspend fun getEasyMissions(): List<Mission> {
        return missionDao.getEasyMissions()
    }

    suspend fun getMediumMissions(): List<Mission> {
        return missionDao.getMediumMissions()
    }

    suspend fun getHardMissions(): List<Mission> {
        return missionDao.getHardMissions()
    }

}

suspend fun getNewMission(repository: MissionRepository): Mission {
    val missionList = repository.getUncompletedMissions()
    if (missionList.isEmpty()) {
        return Mission(id = 0, text = "No challenges available", difficulty = "Easy", completed = false, dateCompleted = "")
    }
    return missionList[Random.nextInt(0, missionList.size)]
}