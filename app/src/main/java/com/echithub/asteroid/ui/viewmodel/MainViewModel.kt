package com.echithub.asteroid.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.echithub.asteroid.data.AppDatabase
import com.echithub.asteroid.data.api.AsteroidApiService
import com.echithub.asteroid.data.api.Response.BaseResponse
import com.echithub.asteroid.data.api.Response.CloseApproachData
import com.echithub.asteroid.data.api.Response.EstimatedDiameter
import com.echithub.asteroid.data.api.Response.RelativeVelocity
import com.echithub.asteroid.data.model.Asteroid
import com.echithub.asteroid.data.model.PictureOfDay
import com.echithub.asteroid.data.repo.AsteroidRepo
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
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

        getAsteroidsFromApi()
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
                        Log.i("Asteroid Api Result : ",t.nearEarthObjects.toString())

                        var asteroidList = ArrayList<Asteroid>()

                        val resultJson: LinkedTreeMap<String,ArrayList<Any>>? = t.nearEarthObjects as? LinkedTreeMap<String,ArrayList<Any>>
                        if (resultJson != null) {
                            for ((key,value) in resultJson){
                                Log.i("Asteroid Key Map : ","$key")
                                for (asteroid in value){

                                    val currentAsteroid: LinkedTreeMap<String,Any>? = asteroid as? LinkedTreeMap<String,Any>
                                    val codeName = currentAsteroid?.get("name")
                                    val id = currentAsteroid?.get("id").toString().toLong()
                                    val neoReferenceId = currentAsteroid?.get("neo_reference_id")
                                    val nasaJplUrl = currentAsteroid?.get("nasa_jpl_url")
                                    val absoluteMagnitude = currentAsteroid?.get("absolute_magnitude_h")

                                    val estimatedDiameter:EstimatedDiameter = Gson()
                                        .fromJson(currentAsteroid?.get("estimated_diameter").toString(),EstimatedDiameter::class.java)

                                    val diameter = estimatedDiameter.kilometers?.estimatedDiameterMax
                                    val isPotentiallyHazardous = currentAsteroid?.get("is_potentially_hazardous_asteroid") as Boolean

                                    // Closing Approach Date
                                    val dateData = currentAsteroid?.get("close_approach_data") as List<*>
                                    val actualData = dateData[0] as LinkedTreeMap<String,Any>
                                    val closeApproachDate = actualData?.get("close_approach_date").toString()

                                    // Relative Velocity
                                    val velocityData = actualData?.get("relative_velocity") as LinkedTreeMap<String,Any>
                                    val relativeVelocity = velocityData["miles_per_hour"].toString().toDouble()
                                    Log.i("Asteroid Velocity",relativeVelocity.toString())

                                    //Distance From Earth
                                    val distance = actualData?.get("miss_distance") as LinkedTreeMap<String,Any>
                                    val distanceFromEarth = distance["miles"].toString().toDouble()
                                    Log.i("Asteroid Distance",distanceFromEarth.toString())
//


                                    val isSentryObject = currentAsteroid?.get("is_sentry_object")

                                    val asteroidToAdd = Asteroid(
                                        id = id,
                                        codename = codeName as String,
                                        closeApproachDate = closeApproachDate,
                                        estimatedDiameter = diameter.toString().toDouble(),
                                        absoluteMagnitude = absoluteMagnitude.toString().toDouble(),
                                        relativeVelocity = relativeVelocity,
                                        distanceFromEarth = distanceFromEarth,
                                        isPotentiallyHazardous = isPotentiallyHazardous
                                    )
                                    asteroidList.add(asteroidToAdd)
                                    Log.i("Asteroid Asteroid : ",currentAsteroid?.get("name").toString())
                                }

                            }
                        }
//
                        Log.i("Asteroid Completed: ",asteroidList.toString())
                        Log.i("Asteroid Completed: ","Completed Download")
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