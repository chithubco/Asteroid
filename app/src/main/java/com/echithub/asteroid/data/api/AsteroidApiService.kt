package com.echithub.asteroid.data.api

import com.echithub.asteroid.data.model.PictureOfDay
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AsteroidApiService {

    private val BASE_URL = "https://api.nasa.gov"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(AsteroidApi::class.java)

    fun getPictureOfDay(): Single<PictureOfDay>{
        return api.getPictureOfDay()
    }
}