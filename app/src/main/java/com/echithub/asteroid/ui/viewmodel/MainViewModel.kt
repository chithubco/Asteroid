package com.echithub.asteroid.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.api.AsteroidApiService
import com.echithub.asteroid.data.api.Response.BaseResponse
import com.echithub.asteroid.data.api.Response.CloseApproachData
import com.echithub.asteroid.data.api.Response.EstimatedDiameter
import com.echithub.asteroid.data.api.Response.RelativeVelocity
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import com.echithub.asteroid.data.repo.AsteroidRepo
import com.echithub.asteroid.util.asteroidRetrieved
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    lateinit var readAllData: LiveData<List<Asteroid>>
    private lateinit var repo: AsteroidRepo

    private val asteroidService = AsteroidApiService()
    private val disposable = CompositeDisposable()

    val asteroids = MutableLiveData<List<Asteroid>>()
    val pictureOfDay = MutableLiveData<PictureOfDay>()
    val hasError = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        val asteroidDao = AppDatabase.getDatabase(getApplication()).asteroidDao
        repo = AsteroidRepo(asteroidDao)
        readAllData = repo.readAllData
    }

    fun storeAsteroidLocally(list: List<Asteroid>){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteAllAsteroidFromDatabase()
            val result = repo.addAsteroid(*list.toTypedArray())
        }
    }

    fun refresh(){
        hasError.value = false
        isLoading.value = true
        getPictureOfDayFromApi()
        getAsteroidsFromApi()

    }

    fun getPictureOfDayFromApi(){
        isLoading.value = true

        disposable.add(
            asteroidService.getPictureOfDay()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<PictureOfDay>(){
                    override fun onSuccess(t: PictureOfDay) {
                        pictureOfDay.value = t
                        isLoading.value = false
                        hasError.value = false
                        Log.i("Asteroid Picture: ",t.toString())
                    }

                    override fun onError(e: Throwable) {
                        isLoading.value = false
                        hasError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }

    fun getAsteroidsFromApi(){
        isLoading.value = true
        Log.i("Asteroid Api Start : ","Starting Download")
        disposable.add(
            asteroidService.getAsteroids()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<BaseResponse>(){
                    override fun onSuccess(t: BaseResponse) {
                        isLoading.value = false
                        hasError.value = false

                        val asteroidList = asteroidRetrieved(t.nearEarthObjects as? LinkedTreeMap<String,ArrayList<Any>>)

                        storeAsteroidLocally(asteroidList)
                        asteroids.value = asteroidList
                    }

                    override fun onError(e: Throwable) {
                        isLoading.value = false
                        hasError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}