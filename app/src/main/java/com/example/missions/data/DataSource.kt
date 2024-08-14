package com.example.missions.data

object DataSource {
    /*val missions = listOf<String>(
        "Talk to a stranger today.",
        "Take a massive dump today.",
        "Have a wee today."
    )*/
    val missions = listOf<Mission>(
        Mission("Talk to a stranger today.", "Easy"),
        Mission("Take a massive dump today.", "Medium"),
        Mission("Have a wee today.", "Hard")
    )
    val difficulties = listOf<String>(
        "Easy",
        "Medium",
        "Hard"
    )
}