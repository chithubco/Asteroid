package com.echithub.asteroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay

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
    fun getAsteroidWithId(key:Long): Asteroid

    @Query("DELETE FROM asteroid_table")
    fun deleteAll()

    /**
     * Picture of Day Methods
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(vararg pictures: PictureOfDay)

    @Delete
    fun deletePictureOfDay(picture: PictureOfDay)

    @Query("SELECT * FROM picture_of_day_table ORDER BY uuid desc")
    fun getPictureOfDay(): PictureOfDay

}