package com.example.missions.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.missions.data.HistoryMission

@Dao
interface HistoryMissionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyMission: HistoryMission)

    @Update
    suspend fun update(historyMission: HistoryMission)

    @Delete
    suspend fun delete(historyMission: HistoryMission)

    @Query("SELECT * FROM history_missions ORDER BY dateCompleted DESC")
    suspend fun getAllHistoryMissions(): List<HistoryMission>

}