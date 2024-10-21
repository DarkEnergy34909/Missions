package com.example.missions.network

import com.example.missions.data.Mission
import com.example.missions.data.PostMission
import com.example.missions.data.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MissionsApiService {
    @GET("/user")
    suspend fun getUserData(): String

    @Headers("Content-Type: application/json")
    @POST("/missions")
    suspend fun postMission(@Body mission: PostMission): String

    @Headers("Content-Type: application/json")
    @POST("/signup")
    suspend fun postSignup(@Body user: User): String

    @Headers("Content-Type: application/json")
    @POST("/login")
    suspend fun postLogin(@Body user: User): String
}