package com.echithub.asteroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "picture_of_day_table")
data class PictureOfDay(
    @SerializedName("media_type")
    val mediaType: String,
    val title: String,
    val url: String,
    val explanation: String?,
    val copyright: String?
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
