package com.example.missions.data.repository

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.missions.data.HistoryMission
import com.example.missions.data.Mission
import com.example.missions.data.datastore.UserPreferencesRepositorySingleton
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class MissionRepository(context: Context) {
    private val missionDao = MissionDatabase.getDatabase(context).missionDao()
    private val historyMissionDao = MissionDatabase.getDatabase(context).historyMissionDao()

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
            return Mission(id = 0, text = "No challenges available", difficulty = "Easy", dateCompleted = "")
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

    suspend fun getCompletedAndFailedMissions(): List<Mission> {
        return missionDao.getCompletedAndFailedMissions()
    }

    private suspend fun getUncompletedEasyMissions(): List<Mission> {
        return missionDao.getUncompletedEasyMissions()
    }

    private suspend fun getUncompletedMediumMissions(): List<Mission> {
        return missionDao.getUncompletedMediumMissions()
    }

    private suspend fun getUncompletedHardMissions(): List<Mission> {
        return missionDao.getUncompletedHardMissions()
    }

    suspend fun getNewMission(context: Context): Mission {
        val easyMissionList = getUncompletedEasyMissions()
        val mediumMissionList = getUncompletedMediumMissions()
        val hardMissionList = getUncompletedHardMissions()

        if (easyMissionList.isEmpty() && mediumMissionList.isEmpty() && hardMissionList.isEmpty()) {
            return Mission(id = 0, text = "No challenges available", difficulty = "Easy", dateCompleted = "")
        }

        val userPreferencesRepository = UserPreferencesRepositorySingleton.getInstance(context)
        val socialScore = userPreferencesRepository.socialScoreFlow.first()

        val easyProbability = 0.75f * (1 - socialScore)
        val mediumProbability = 0.25f * (1 - socialScore) + (0.5f * socialScore)
        val hardProbability = 0.5f * socialScore

        val randomNumber: Float = Random.nextFloat()
        when (randomNumber) {
             in 0.0f..easyProbability -> {
                 if (!easyMissionList.isEmpty()) {
                     return easyMissionList[Random.nextInt(0, easyMissionList.size)]
                 }
                 else if (!mediumMissionList.isEmpty()) {
                     return mediumMissionList[Random.nextInt(0, mediumMissionList.size)]
                 }
                 else {
                     return hardMissionList[Random.nextInt(0, hardMissionList.size)]
                 }
             }
            in easyProbability..(easyProbability + mediumProbability) -> {
                if (!mediumMissionList.isEmpty()) {
                    return mediumMissionList[Random.nextInt(0, mediumMissionList.size)]
                }
                else if (!easyMissionList.isEmpty()) {
                    return easyMissionList[Random.nextInt(0, easyMissionList.size)]
                }
                else {
                    return hardMissionList[Random.nextInt(0, hardMissionList.size)]
                }
            }
            in (easyProbability + mediumProbability)..1.0f -> {
                if (!hardMissionList.isEmpty()) {
                    return hardMissionList[Random.nextInt(0, hardMissionList.size)]
                }
                else if (!mediumMissionList.isEmpty()) {
                    return mediumMissionList[Random.nextInt(0, mediumMissionList.size)]
                }
                else {
                    return easyMissionList[Random.nextInt(0, easyMissionList.size)]
                }
            }
            else -> {
                return Mission(id = 0, text = "Wtf", difficulty = "Easy", dateCompleted = "")
            }

        }

    }

    // History mission stuff

    suspend fun insertHistoryMission(historyMission: HistoryMission) {
        historyMissionDao.insert(historyMission)
    }

    suspend fun updateHistoryMission(historyMission: HistoryMission) {
        historyMissionDao.update(historyMission)
    }

    suspend fun deleteHistoryMission(historyMission: HistoryMission) {
        historyMissionDao.delete(historyMission)
    }

    suspend fun getAllHistoryMissions(): List<HistoryMission> {
        return historyMissionDao.getAllHistoryMissions()
    }

}

suspend fun getNewMission(repository: MissionRepository): Mission {
    val missionList = repository.getUncompletedMissions()
    if (missionList.isEmpty()) {
        return Mission(id = 0, text = "No challenges available", difficulty = "Easy", dateCompleted = "")
    }
    return missionList[Random.nextInt(0, missionList.size)]
}

