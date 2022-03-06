package com.echithub.asteroid.data.api.Response

import com.google.gson.annotations.SerializedName

data class EstimatedDiameter(
    val kilometers: EstimatedDiameterMeasurement?,
    val meters: EstimatedDiameterMeasurement?,
    val miles: EstimatedDiameterMeasurement?,
    val feet: EstimatedDiameterMeasurement?,
)
