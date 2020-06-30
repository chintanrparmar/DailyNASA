package com.crp.dailynasa.data.api

import com.crp.dailynasa.data.model.ResponseData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NasaAPI {

    //https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2020-06-28


    @GET("apod?")
    suspend fun getImageResponse(
        @Query("api_key") apiKey: String,
        @Query("date") dateString: String
    ): ResponseData
}