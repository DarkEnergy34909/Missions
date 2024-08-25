package com.example.missions.network

import com.example.missions.data.Mission
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MissionsApiService {
    @GET("/users")
    suspend fun getUserData(): String

    @POST("/missions")
    suspend fun postMission(@Body mission: Mission): Call<ResponseBody>
}