package com.example.missions.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.example.missions.MissionStates
import com.example.missions.data.MissionRepository
import com.example.missions.data.UserPreferencesRepositorySingleton
import com.example.missions.data.getNewMission
import com.example.missions.getDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.sql.Time
import java.util.concurrent.TimeUnit

class NewMissionWorker(
    context: Context,
    params: WorkerParameters
    ): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        val missionRepository = MissionRepository(context)

        val userPreferencesRepository = UserPreferencesRepositorySingleton.getInstance(context)
        val currentMissionId = userPreferencesRepository.missionIdFlow.firstOrNull()
        //val missionState = userPreferencesRepository.missionStateFlow.first()

        if (currentMissionId != null) {
            val currentMission = missionRepository.getMission(currentMissionId)

            if (!currentMission.completed) {
                currentMission.failed = true
                currentMission.dateCompleted = getDate()
            }

            missionRepository.updateMission(currentMission)

            // TODO: Handle errors
            val newMission = getNewMission(missionRepository)

            if (newMission.id != -1) {
                userPreferencesRepository.saveMissionId(missionId = newMission.id)
            }
        }

        val missionState = userPreferencesRepository.missionStateFlow.first()
        if (missionState == MissionStates.MISSION_UNDEFINED.ordinal) {
            userPreferencesRepository.saveStreak(streak = 0)
        }

        userPreferencesRepository.saveMissionState(MissionStates.MISSION_UNDEFINED.ordinal)


        return Result.success()
    }
}

fun scheduleMissionChange(context: Context) {
    val initialDelay = getDelay(16, 37)

    val newMissionWorkRequest: WorkRequest = PeriodicWorkRequestBuilder<NewMissionWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(newMissionWorkRequest)
    println("Mission change scheduled")
}