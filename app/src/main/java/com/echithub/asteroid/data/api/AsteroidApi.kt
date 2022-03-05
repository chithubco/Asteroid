package com.echithub.asteroid.data.api

import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import io.reactivex.Single
import retrofit2.http.GET

interface AsteroidApi {
    @GET("neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=nawqo1ORHNvzDZW4GaUkLjhPNmdzx05UBzBLngVH")
    fun getAsteroid(): Single<List<Asteroid>>

    @GET("planetary/apod?api_key=nawqo1ORHNvzDZW4GaUkLjhPNmdzx05UBzBLngVH")
    fun getPictureOfDay(): Single<PictureOfDay>
}