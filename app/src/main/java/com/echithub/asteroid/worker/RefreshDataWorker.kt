package com.echithub.asteroid.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.repo.AsteroidRepo
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context,params: WorkerParameters)
    : CoroutineWorker(appContext,params){
    override suspend fun doWork(): Result {
        val asteroidDao = AppDatabase.getDatabase(applicationContext)
        val repo = AsteroidRepo(asteroidDao)

        return try {
            repo.refresh()
            Result.success()
        }catch(exception: HttpException) {
            Result.retry()
        }
    }

}