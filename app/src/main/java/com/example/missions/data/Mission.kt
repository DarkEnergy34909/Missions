package com.example.missions.data

import java.util.Date


class Mission(val text: String, val difficulty: String) {
    var completed = false
    lateinit var dateCompleted: String

    fun complete() {
        completed = true

        dateCompleted = "" // TODO: Implement dateCompleted
    }

}