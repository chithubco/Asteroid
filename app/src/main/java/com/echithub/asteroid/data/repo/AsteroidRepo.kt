package com.echithub.asteroid.data.repo

import androidx.lifecycle.LiveData
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.api.AsteroidApiService
import com.echithub.asteroid.data.api.Response.BaseResponse
import com.echithub.asteroid.data.dao.AsteroidDao
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.util.asteroidRetrieved
import com.google.gson.internal.LinkedTreeMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching asteroids
 */
//class AsteroidRepo(private val  asteroidDao: AsteroidDao) {
class AsteroidRepo(private val  database: AppDatabase) {

    private val asteroidDao = database.asteroidDao
    val readAllData: LiveData<List<Asteroid>> = asteroidDao.getAll()
    private val disposable = CompositeDisposable()
    private val asteroidService = AsteroidApiService()

    suspend fun addAsteroid(vararg asteroids: Asteroid):List<Long>{
        return asteroidDao.insertAsteroid(*asteroids)
    }

    suspend fun deleteAllAsteroidFromDatabase(){
        asteroidDao.deleteAll()
    }

//    suspend fun getAsteroidFromRemoteApi():ArrayList<Asteroid>{
//        withContext(Dispatchers.IO){
//            disposable.add(
//                asteroidService.getAsteroids()
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(object: DisposableSingleObserver<BaseResponse>(){
//                        override fun onSuccess(t: BaseResponse) {
//
//                            val asteroidList = asteroidRetrieved(t.nearEarthObjects as? LinkedTreeMap<String,ArrayList<Any>>)
//
////                            storeAsteroidLocally(asteroidList)
//
//                        }
//
//                        override fun onError(e: Throwable) {
//                            e.printStackTrace()
//                        }
//
//                    })
//            )
//        }
//    }

    suspend fun storeAsteroidLocally(list:List<Asteroid>){
        withContext(Dispatchers.IO){
            deleteAllAsteroidFromDatabase()
            addAsteroid(*list.toTypedArray())
        }
    }
}