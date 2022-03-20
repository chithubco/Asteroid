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
    lateinit var readPictureOfDay: LiveData<PictureOfDay>

    private var repo: AsteroidRepo = AsteroidRepo(AppDatabase.getDatabase(getApplication()))

    private val asteroidService = AsteroidApiService()
    private val disposable = CompositeDisposable()

    val pictureOfDay = MutableLiveData<PictureOfDay>()
    val hasError = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val clearAll = MutableLiveData<Boolean>()
    val searchDate = MutableLiveData<String>()
    val asteroidList = MutableLiveData<List<Asteroid>>()

    private val TAG = "Asteroid ViewModel"

    init {
        readAllData = repo.readAllData
        clearAll.value = false
    }


    fun refresh(){
        hasError.value = false
        isLoading.value = true
       viewModelScope.launch {
           repo.refresh()
           repo.refreshPictureOfDay()
           readAllData = repo.readAllData
       }

    }

    fun clearData(){
        readAllData = repo.readAllData
        clearAll.value = false
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
                    }

                    override fun onError(e: Throwable) {
                        isLoading.value = false
                        hasError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }

    fun refreshData(){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repo.refresh()
        }
    }

    fun filterDateByDate(){
        viewModelScope.launch(Dispatchers.IO) {
            searchDate.value?.let {
                asteroidList.postValue(repo.getAsteroidWithCreatedDate(it))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}