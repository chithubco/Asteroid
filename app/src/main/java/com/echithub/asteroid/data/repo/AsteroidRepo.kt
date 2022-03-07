package com.echithub.asteroid.data.repo

import androidx.lifecycle.LiveData
import com.echithub.asteroid.data.dao.AsteroidDao
import com.echithub.asteroid.data.model.Asteroid

class AsteroidRepo(private val  asteroidDao: AsteroidDao) {

    val readAllData: LiveData<List<Asteroid>> = asteroidDao.getAll()

    suspend fun addAsteroid(vararg asteroids: Asteroid):List<Long>{
        return asteroidDao.insertAsteroid(*asteroids)
    }
}