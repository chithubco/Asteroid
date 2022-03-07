package com.echithub.asteroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.echithub.asteroid.data.model.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid_table")
    fun  getAll(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(vararg asteroids: Asteroid):List<Long>

    @Delete
    fun delete(asteroid: Asteroid)

//    @Update
//    suspend fun update(asteroid: Asteroid)
//
    @Query("SELECT * FROM asteroid_table WHERE id=:key")
    fun get(key:Long): Asteroid?

//    @Query("DELETE FROM asteroid_table")
//    suspend fun deleteAll

}