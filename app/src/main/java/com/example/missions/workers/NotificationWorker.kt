package com.example.missions.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ContextMenu
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.example.missions.MissionStates
import com.example.missions.data.UserPreferencesRepository
import com.example.missions.data.UserPreferencesRepositorySingleton
import com.example.missions.userPreferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

const val TAG = "NotificationWorker"

//const val USER_PREFERENCE_NAME = "user_preferences"
//val Context.userPreferencesDataStore by preferencesDataStore(name = USER_PREFERENCE_NAME)

class NotificationWorker(
    context: Context,
    params: WorkerParameters,

    ): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        //val userPreferencesRepository = UserPreferencesRepository(context.userPreferencesDataStore) // TODO: DELETE THIS!!!
        val userPreferencesRepository = UserPreferencesRepositorySingleton.getInstance(context)
        val missionState = userPreferencesRepository.missionStateFlow.first()

        if (missionState == MissionStates.MISSION_UNDEFINED.ordinal) {
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "mission_reminder_channel"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Challenge Reminder"
            val descriptionText = "Reminder to completed your daily challenge"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                channelId,
                name,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Daily Challenge Reminder")
            .setContentText("Don't forget to complete your daily challenge!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with (NotificationManagerCompat.from(applicationContext)) {
            try {
                notify(notificationId, builder.build())
            }
            catch(exception: SecurityException) {
                Log.e(TAG, "Notifications are not enabled for this app.")
            }

        }

    }
}

fun scheduleNotification(context: Context) {
    val currentTime = System.currentTimeMillis()
    val calendar = Calendar.getInstance().apply {
        timeInMillis = currentTime
        set(Calendar.HOUR_OF_DAY, 16)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    if (calendar.timeInMillis < currentTime) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = calendar.timeInMillis - currentTime

    val notificationWorkRequest: WorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(notificationWorkRequest)
    println("Notification scheduled")
}