package com.echithub.asteroid.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.repo.AsteroidRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val  asteroidLiveData =  MutableLiveData<Asteroid>()

    private var repo: AsteroidRepo = AsteroidRepo(AppDatabase.getDatabase(getApplication()))


    private suspend fun fetch(asteroidId: Long):Asteroid{
        val asteroid = repo.getAsteroidWithId(asteroidId)
        Log.i("Asteroid Fetch :",asteroid.toString())
        return asteroid
    }

    fun refresh(asteroidId: Long){
        viewModelScope.launch (Dispatchers.IO){
            asteroidLiveData.postValue(fetch(asteroidId))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}