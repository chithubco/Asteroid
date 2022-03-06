package com.echithub.asteroid.data.api.Response

import com.google.gson.annotations.SerializedName

data class EstimatedDiameterMeasurement(
    @SerializedName("estimated_diameter_min")
    val estimatedDiameterMin: Double?,
    @SerializedName("estimated_diameter_max")
    val estimatedDiameterMax: Double?
)