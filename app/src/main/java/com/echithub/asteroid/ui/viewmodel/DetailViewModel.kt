package com.echithub.asteroid.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.echithub.asteroid.data.model.Asteroid

class DetailViewModel:ViewModel() {

    val asteroidLiveData = MutableLiveData<Asteroid>()

    fun fetch() {
        val asteroid = Asteroid(123,"Destroyer","12 June 2022",5.6,3.5,3.6,56.7,true)
        asteroidLiveData.value = asteroid
    }

}