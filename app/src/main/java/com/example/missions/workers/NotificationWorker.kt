package com.example.missions.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.missions.MainActivity
import com.example.missions.MissionStates
import com.example.missions.data.datastore.UserPreferencesRepositorySingleton
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

        //val userPreferencesRepository = UserPreferencesRepository(context.userPreferencesDataStore)
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
            val descriptionText = "Reminder to complete your daily challenge"
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

        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Daily Challenge Reminder")
            .setContentText("Don't forget to complete your daily challenge!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

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

fun getDelay(
    hours: Int,
    minutes: Int,
    seconds: Int = 0
): Long {
    val currentTime = System.currentTimeMillis()
    val calendar = Calendar.getInstance().apply {
        timeInMillis = currentTime
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, seconds)
    }

    if (calendar.timeInMillis < currentTime) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = calendar.timeInMillis - currentTime
    return initialDelay
}

fun scheduleNotification(context: Context) {
    val initialDelay = getDelay(16, 0)

    val notificationWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
        .addTag("notification")
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "notification",
        androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
        notificationWorkRequest
    )
    //WorkManager.getInstance(context).enqueue(notificationWorkRequest)

    println("Notification scheduled")
}