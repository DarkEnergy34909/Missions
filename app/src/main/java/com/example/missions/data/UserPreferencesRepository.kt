package com.example.missions.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val streakFlow: Flow<Int> = dataStore.data
        .catch{ exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            }
            else {
                throw exception
            }

        }
        .map { preferences ->
        preferences[STREAK] ?: 0
    }

    val missionStateFlow: Flow<Int> = dataStore.data
        .catch{ exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            }
            else {
                throw exception
            }

        }
        .map { preferences ->
        preferences[MISSION_STATE] ?: 0
    }

    private companion object {
        val STREAK = intPreferencesKey("streak")
        val MISSION_STATE = intPreferencesKey("mission_state")
        const val TAG = "UserPreferencesRepository"
    }

    suspend fun saveStreak(streak: Int) {
        dataStore.edit { preferences ->
            preferences[STREAK] = streak
        }
    }

    suspend fun saveMissionState(missionState: Int) {
        dataStore.edit { preferences ->
            preferences[MISSION_STATE] = missionState
        }
    }
}