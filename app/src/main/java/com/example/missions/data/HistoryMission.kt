package com.example.missions.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_missions")
data class HistoryMission(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val difficulty: String,
    var success: Boolean = false,
    var dateCompleted: String = ""
)
