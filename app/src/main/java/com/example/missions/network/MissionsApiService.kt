package com.example.missions.network

import com.example.missions.data.Mission
import com.example.missions.data.PostMission
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MissionsApiService {
    @GET("/users")
    suspend fun getUserData(): String

    @Headers("Content-Type: application/json")
    @POST("/missions")
    suspend fun postMission(@Body mission: PostMission): String
}