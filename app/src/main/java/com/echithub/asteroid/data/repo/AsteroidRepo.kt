package com.echithub.asteroid.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.api.AsteroidApiService
import com.echithub.asteroid.data.api.Response.BaseResponse
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import com.echithub.asteroid.util.Constants
import com.echithub.asteroid.util.asteroidRetrieved
import com.echithub.asteroid.util.getNextSevenDaysFormattedDates
import com.google.gson.internal.LinkedTreeMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Repository for fetching asteroids
 */
//class AsteroidRepo(private val  asteroidDao: AsteroidDao) {
class AsteroidRepo(private val database: AppDatabase) {

    private val TAG = "Asteroid Repo"
    private val asteroidDao = database.asteroidDao
    val readAllData: LiveData<List<Asteroid>> = asteroidDao.getAll()

    private val disposable = CompositeDisposable()
    private val asteroidService = AsteroidApiService()

    private var asteroids: List<Asteroid>? = null
    private var pictures: PictureOfDay? = null

    private val myCoroutineScope = CoroutineScope(Dispatchers.IO)

    suspend fun addAsteroid(vararg asteroids: Asteroid): List<Long> {
        return asteroidDao.insertAsteroid(*asteroids)
    }

    private suspend fun addPictureOfDay(vararg pictures: PictureOfDay) {
        asteroidDao.insertPictureOfDay(*pictures)
    }


    suspend fun deleteAllAsteroidFromDatabase() {
        asteroidDao.deleteAll()
    }

    fun getAsteroidWithId(asteroidId: Long): Asteroid {
        return asteroidDao.getAsteroidWithId(asteroidId)
    }

    fun getAsteroidWithCreatedDate(key: String): List<Asteroid>{
        return asteroidDao.getAsteroidWithCreatedDate(key)
    }

    /**
     * Get Api with Params
     */
    private suspend fun getAsteroidFromRemoteApiWithParams(
        startDate: String,
        endDate: String,
        apiKey: String
    ) {
        withContext(Dispatchers.IO) {
            disposable.add(
                asteroidService.getAsteroidForDay(startDate, endDate, apiKey)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<BaseResponse>() {
                        override fun onSuccess(t: BaseResponse) {
                            asteroids = asteroidRetrieved(t.nearEarthObjects as? LinkedTreeMap<String, ArrayList<Any>>)
                            storeToDb(asteroids as List<Asteroid>)
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                    })
            )

        }

    }

    fun getPictureOfDayFromApi() {

        disposable.add(
            asteroidService.getPictureOfDay()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PictureOfDay>() {
                    override fun onSuccess(t: PictureOfDay) {
                        pictures = t
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        )
    }

    suspend fun refresh() {
        val listOfDates = getNextSevenDaysFormattedDates()
        getAsteroidFromRemoteApiWithParams(
            listOfDates[0],
            listOfDates[listOfDates.size - 1],
            Constants.API_KEY
        )
    }


    suspend fun refreshPictureOfDay() {
        getPictureOfDayFromApi()
        pictures?.let { storePictureOfDayLocally(it) }
    }


    private fun storeToDb(list: List<Asteroid>) {
        myCoroutineScope.launch {
            deleteAllAsteroidFromDatabase()
            addAsteroid(*list.toTypedArray())
        }
    }

    private suspend fun storePictureOfDayLocally(list: PictureOfDay) {
        withContext(Dispatchers.IO) {
            addPictureOfDay(list)
        }
    }
}