package com.echithub.asteroid.util

import android.os.Build
import android.util.Log
import com.echithub.asteroid.data.api.Response.EstimatedDiameter
import com.echithub.asteroid.data.model.Asteroid
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.echithub.asteroid.util.Constants
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

fun asteroidRetrieved(resultJson: LinkedTreeMap<String, ArrayList<Any>>?)
        :ArrayList<Asteroid>{
    var asteroidList = ArrayList<Asteroid>()
    if (resultJson != null) {
        for ((key,value) in resultJson){
//            Log.i("Asteroid Key Map : ","$key")
            val createdDate = key

            for (asteroid in value){

                val currentAsteroid: LinkedTreeMap<String,Any>? = asteroid as? LinkedTreeMap<String,Any>
                val codeName = currentAsteroid?.get("name")
                val id = currentAsteroid?.get("id").toString().toLong()
                val neoReferenceId = currentAsteroid?.get("neo_reference_id")
                val nasaJplUrl = currentAsteroid?.get("nasa_jpl_url")
                val absoluteMagnitude = currentAsteroid?.get("absolute_magnitude_h")

                val estimatedDiameter: EstimatedDiameter = Gson()
                    .fromJson(currentAsteroid?.get("estimated_diameter").toString(),
                        EstimatedDiameter::class.java)

                val diameter = estimatedDiameter.kilometers?.estimatedDiameterMax
                val isPotentiallyHazardous = currentAsteroid?.get("is_potentially_hazardous_asteroid") as Boolean

                // Closing Approach Date
                val dateData = currentAsteroid?.get("close_approach_data") as List<*>
                val actualData = dateData[0] as LinkedTreeMap<String,Any>
                val closeApproachDate = actualData?.get("close_approach_date").toString()

                // Relative Velocity
                val velocityData = actualData?.get("relative_velocity") as LinkedTreeMap<String,Any>
                val relativeVelocity = velocityData["miles_per_hour"].toString().toDouble()
//                Log.i("Asteroid Velocity",relativeVelocity.toString())

                //Distance From Earth
                val distance = actualData?.get("miss_distance") as LinkedTreeMap<String,Any>
                val distanceFromEarth = distance["miles"].toString().toDouble()
//                Log.i("Asteroid Distance",distanceFromEarth.toString())
//


                val isSentryObject = currentAsteroid?.get("is_sentry_object")

//                val imageUrl = currentAsteroid?.get("nasa_jpl_url") as String
                val imageUrl = if (!isPotentiallyHazardous) "https://phantom-marca.unidadeditorial.es/886eaf390397f7aae414d370e57020b8/resize/660/f/webp/assets/multimedia/imagenes/2022/01/12/16420146525312.jpg" else "https://cdn.dnaindia.com/sites/default/files/styles/full/public/2021/12/16/1010226-asteroid-g62225b7ab1280.jpg"

                val asteroidToAdd = Asteroid(
                    id = id,
                    codename = codeName as String,
                    closeApproachDate = closeApproachDate,
                    estimatedDiameter = diameter.toString().toDouble(),
                    absoluteMagnitude = absoluteMagnitude.toString().toDouble(),
                    relativeVelocity = relativeVelocity,
                    distanceFromEarth = distanceFromEarth,
                    isPotentiallyHazardous = isPotentiallyHazardous,
                    url = imageUrl,
                    createdDate = createdDate
                )
                asteroidList.add(asteroidToAdd)
//                Log.i("Asteroid Asteroid : ",currentAsteroid?.get("name").toString())
            }

        }
    }
    return asteroidList
//
}

fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}