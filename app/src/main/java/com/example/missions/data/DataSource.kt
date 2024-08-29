package com.example.missions.data

import com.example.missions.data.repository.MissionRepository

object DataSource {
    /*val missions = listOf<String>(
        "Talk to a stranger today.",
        "Take a massive dump today.",
        "Have a wee today."
    )*/
    val missions = listOf<Mission>(
        //Mission(text = "Talk to a stranger today.", difficulty = "Easy"),
        //Mission(text = "Take a massive dump today.", difficulty = "Medium"),
        //Mission(text = "Have a wee today.", difficulty = "Hard"),

        Mission(text = "Talk to a stranger today", difficulty = "Hard"),
        Mission(text = "Go for a walk today", difficulty = "Easy"),
        Mission(text = "Compliment someone today", difficulty = "Hard"),
        Mission(text = "Greet a stranger today", difficulty = "Medium"),
        Mission(text = "Smile at someone today", difficulty = "Easy"),
        Mission(text = "Message an old friend today", difficulty = "Medium"),

        /*Mission(text = "Don't be a bitch", difficulty = "Hard"),
        Mission(text = "Play The Strongest Battlegrounds", difficulty = "Easy"),
        Mission(text = "Become a billionaire", difficulty = "Medium"),
        Mission(text = "Talk to Tamara", difficulty = "Easy"),
        Mission(text = "Meet Jeff Bezos", difficulty = "Easy"),
        Mission(text = "Touch grass", difficulty = "Medium"),
        Mission(text = "Make £1", difficulty = "Hard")*/



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