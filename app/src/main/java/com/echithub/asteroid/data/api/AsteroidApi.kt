package com.echithub.asteroid.data.api

import com.echithub.asteroid.data.api.Response.BaseResponse
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.GET

interface AsteroidApi {
    @GET("neo/rest/v1/feed?start_date=2021-09-07&end_date=2021-09-08&api_key=nawqo1ORHNvzDZW4GaUkLjhPNmdzx05UBzBLngVH")
    fun getAsteroid(): Single<BaseResponse>

    @GET("planetary/apod?api_key=nawqo1ORHNvzDZW4GaUkLjhPNmdzx05UBzBLngVH")
    fun getPictureOfDay(): Single<PictureOfDay>
}