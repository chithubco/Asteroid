package com.echithub.asteroid.data.api.Response

import com.google.gson.annotations.SerializedName

data class MissDistance(
    val astronomical: String?,
    val lunar: String?,
    val kilometers: String?,
    val miles: String?,
)
