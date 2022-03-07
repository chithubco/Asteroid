package com.echithub.asteroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.echithub.asteroid.data.dao.AsteroidDao
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay

@Database(entities = [Asteroid::class,PictureOfDay::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /*
        Create the database
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "asteroid_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}