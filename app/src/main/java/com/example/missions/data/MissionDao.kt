package com.example.missions.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mission: Mission)

    @Update
    suspend fun update(mission: Mission)

    @Delete
    suspend fun delete(mission: Mission)

    @Query("SELECT * FROM missions WHERE id = :id")
    fun getMission(id: Int): Flow<Mission>

    @Query("SELECT * FROM missions ORDER BY id")
    fun getAllMissions(): Flow<List<Mission>>

}