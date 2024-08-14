package com.example.missions.data

object DataSource {
    /*val missions = listOf<String>(
        "Talk to a stranger today.",
        "Take a massive dump today.",
        "Have a wee today."
    )*/
    val missions = listOf<Mission>(
        Mission(text = "Talk to a stranger today.", difficulty = "Easy"),
        Mission(text = "Take a massive dump today.", difficulty = "Medium"),
        Mission(text = "Have a wee today.", difficulty = "Hard")
    )
    val difficulties = listOf<String>(
        "Easy",
        "Medium",
        "Hard"
    )
}