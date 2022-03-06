package com.echithub.asteroid.data.api.Response

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class BaseResponse (
    val links: Links?,
    @SerializedName("element_count")
    val elementCount: Int?,
    @SerializedName("near_earth_objects")
    val nearEarthObjects:Any
)