package com.example.goolepolylinedemo

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("maps/api/directions/json")
    fun getRouteForPolyline(@Query("origin") origin: String?,
                            @Query("destination") destination: String?,
                            @Query("mode") mode: String?,
                            @Query("key") apiKey: String?): Call<JsonObject?>?

}