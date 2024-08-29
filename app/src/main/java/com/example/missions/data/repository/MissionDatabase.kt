package com.example.missions.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.missions.data.Mission

@Database(entities = [Mission::class], version = 1, exportSchema = false)
abstract class MissionDatabase: RoomDatabase() {

    abstract fun missionDao(): MissionDao

    companion object {
        @Volatile
        private var Instance: MissionDatabase? = null

        fun getDatabase(context: Context): MissionDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MissionDatabase::class.java, "mission_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it}
            }
        }
    }
}