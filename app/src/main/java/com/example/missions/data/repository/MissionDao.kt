package com.example.missions.data.repository
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.missions.data.Mission

@Dao
interface MissionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mission: Mission)

    @Update
    suspend fun update(mission: Mission)

    @Delete
    suspend fun delete(mission: Mission)

    @Query("SELECT * FROM missions WHERE id = :id")
    suspend fun getMission(id: Int): Mission

    @Query("SELECT * FROM missions ORDER BY id")
    suspend fun getAllMissions(): List<Mission>

    @Query("SELECT * FROM missions WHERE completed = false ORDER BY id")
    suspend fun getUncompletedMissions(): List<Mission>

    @Query("SELECT * FROM missions WHERE completed = true OR failed = true ORDER BY dateCompleted DESC")
    suspend fun getCompletedMissions(): List<Mission>

    @Query("SELECT * FROM missions WHERE difficulty = 'Easy' AND completed = false ORDER BY id")
    suspend fun getUncompletedEasyMissions(): List<Mission>

    @Query("SELECT * FROM missions WHERE difficulty = 'Medium' AND completed = false ORDER BY id")
    suspend fun getUncompletedMediumMissions(): List<Mission>

    @Query("SELECT * FROM missions WHERE difficulty = 'Hard' AND completed = false ORDER BY id")
    suspend fun getUncompletedHardMissions(): List<Mission>

}