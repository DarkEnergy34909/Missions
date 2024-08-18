package com.example.missions.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object UserPreferencesRepositorySingleton {
    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    @Volatile
    private var Instance: UserPreferencesRepository? = null

    fun getInstance(context: Context): UserPreferencesRepository {
        return Instance ?: synchronized(this) {
            val instance = UserPreferencesRepository(context.dataStore)
            Instance = instance
            instance
        }
    }
}