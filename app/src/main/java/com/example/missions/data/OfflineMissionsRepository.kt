package com.example.missions.data

import kotlinx.coroutines.flow.Flow

class OfflineMissionsRepository(private val missionDao: MissionDao): MissionsRepository {
    override fun getAllMissionsStream(): Flow<List<Mission>> = missionDao.getAllMissions()

    override fun getMissionStream(id: Int): Flow<Mission> = missionDao.getMission(id)

    override suspend fun insertMission(mission: Mission) = missionDao.insert(mission)

    override suspend fun deleteMission(mission: Mission) = missionDao.delete(mission)

    override suspend fun updateMission(mission: Mission) = missionDao.update(mission)

}