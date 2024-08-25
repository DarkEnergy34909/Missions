package com.example.missions.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val BASE_URL = "http://192.168.1.136:5000"

internal val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    //.addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object MissionsApi {
    val retrofitService: MissionsApiService by lazy {
        retrofit.create(MissionsApiService::class.java)
    }


}