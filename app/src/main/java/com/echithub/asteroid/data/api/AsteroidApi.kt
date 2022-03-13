package com.echithub.asteroid.data.api

import com.echithub.asteroid.data.api.Response.BaseResponse
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApi {

    @GET("planetary/apod?api_key=nawqo1ORHNvzDZW4GaUkLjhPNmdzx05UBzBLngVH")
    fun getPictureOfDay(): Single<PictureOfDay>

    @GET("neo/rest/v1/feed")
    fun getAsteroidForDay(
        @Query("start_date") startDate: String,
        @Query("start_date") endDate: String,
        @Query("api_key") apiKey: String
    ): Single<BaseResponse>
}