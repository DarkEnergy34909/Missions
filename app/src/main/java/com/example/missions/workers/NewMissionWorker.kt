package com.example.missions.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.missions.MissionStates
import com.example.missions.data.HistoryMission
import com.example.missions.data.repository.MissionRepository
import com.example.missions.data.datastore.UserPreferencesRepositorySingleton
import com.example.missions.data.repository.getNewMission
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

class NewMissionWorker(
    context: Context,
    params: WorkerParameters
    ): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.i("NewMissionWorker", "Worker started")
        val context = applicationContext

        val missionRepository = MissionRepository(context)

        val userPreferencesRepository = UserPreferencesRepositorySingleton.getInstance(context)
        val currentMissionId = userPreferencesRepository.missionIdFlow.first()
        val socialScore = userPreferencesRepository.socialScoreFlow.first()
        val missionState = userPreferencesRepository.missionStateFlow.first()
        //val missionState = userPreferencesRepository.missionStateFlow.first()


        if (currentMissionId != 0) {
            val currentMission = missionRepository.getMission(currentMissionId)

            if (missionState == MissionStates.MISSION_UNDEFINED.ordinal) {
                missionRepository.insertHistoryMission(
                    HistoryMission(
                        text = currentMission.text,
                        difficulty = currentMission.difficulty,
                        success = false,
                        dateCompleted = currentMission.dateCompleted
                    )
                )
            }

            // If mission was failed
            if (!currentMission.completed) {
                currentMission.failed = true
                //currentMission.dateCompleted = getDate()

                // If mission was failed, decrease score
                if (socialScore >= 0.1f) {
                    userPreferencesRepository.saveSocialScore(socialScore = socialScore - 0.1f)
                }
                else {
                    userPreferencesRepository.saveSocialScore(socialScore = 0.0f)
                }
            }

            // If mission was completed
            else {
                // If mission was completed, increase score
                if (socialScore <= 0.9f) {
                    userPreferencesRepository.saveSocialScore(socialScore = socialScore + 0.1f)
                }
                else {
                    userPreferencesRepository.saveSocialScore(socialScore = 1.0f)
                }
            }

            missionRepository.updateMission(currentMission)

            // TODO: Handle errors
            //val newMission = getNewMission(missionRepository)
            val newMission = missionRepository.getNewMission(context)


            if (newMission.id != 0) {
                userPreferencesRepository.saveMissionId(missionId = newMission.id)
            }
        }


        // If user left the mission
        if (missionState == MissionStates.MISSION_UNDEFINED.ordinal) {
            userPreferencesRepository.saveStreak(streak = 0)
        }

        userPreferencesRepository.saveMissionState(MissionStates.MISSION_UNDEFINED.ordinal)


        return Result.success()
    }
}

fun scheduleMissionChange(context: Context) {
    val initialDelay = getDelay(23, 59, 59)

    val newMissionWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NewMissionWorker>(24, TimeUnit.HOURS)
        .addTag("new_mission")
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    //WorkManager.getInstance(context).enqueue(newMissionWorkRequest)
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "new_mission",
        androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
        newMissionWorkRequest
    )
    println("Mission change scheduled")
}