package com.echithub.asteroid.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.api.AsteroidApiService
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import com.echithub.asteroid.data.repo.AsteroidRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

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

    fun refresh(){
        val asteroid1 = Asteroid(123,"Destroyer","12 June 2022",5.6,3.5,3.6,56.7,true)
        val asteroid2 = Asteroid(56,"FunPlace","13 June 2022",5.6,3.5,3.6,56.7,true)
        val asteroid3 = Asteroid(298,"Hercules","13 June 2022",5.6,3.5,3.6,56.7,true)
        val asteroid4 = Asteroid(89,"Zeus","13 June 2022",5.6,3.5,3.6,56.7,true)

//        val asteroidList = arrayListOf<Asteroid>(
//            asteroid1,asteroid2,asteroid3,asteroid4)
        asteroids.value = arrayListOf(
            asteroid1,asteroid2,asteroid3,asteroid4)
        hasError.value = false
        isLoading.value = false

        getPictureOfDayFromApi()
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
                        Log.i("Asteroid Api Result : ",t.toString())
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