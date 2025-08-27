package com.example.missions.data

import com.example.missions.data.repository.MissionRepository

object DataSource {
    val missions = listOf<Mission> (

        Mission(text = "Talk to a stranger today", difficulty = "Hard"),
        Mission(text = "Go for a walk today", difficulty = "Easy"),
        Mission(text = "Compliment someone today", difficulty = "Hard"),
        Mission(text = "Greet a stranger today", difficulty = "Medium"),
        Mission(text = "Smile at someone today", difficulty = "Easy"),
        Mission(text = "Message an old friend today", difficulty = "Medium"),



    )
    val difficulties = listOf<String>(
        "Easy",
        "Medium",
        "Hard"
    )

    suspend fun addMissionsToDatabase(missionRepository: MissionRepository) {
        for (mission in missions) {
            missionRepository.insertMission(mission)
        }
    }
}