package com.echithub.asteroid.data.api.Response

import com.google.gson.annotations.SerializedName

data class RelativeVelocity(
    @SerializedName("kilometers_per_second")
    val kilometersPerSecond: String?,
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: String?,
    @SerializedName("miles_per_hour")
    val milesPerHour: String?
)
