package com.example.missions.data

import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missions")
data class Mission(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val difficulty: String,
    var completed: Boolean = false,
    var failed: Boolean = false,
    var dateCompleted: String = ""
) {
    //var completed = false
    //lateinit var dateCompleted: String;

    //fun complete() {
        //completed = true

        //dateCompleted = "08/07/2006"
    //}

}