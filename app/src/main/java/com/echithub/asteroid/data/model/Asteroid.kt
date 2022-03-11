package com.echithub.asteroid.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "asteroid_table")
data class Asteroid(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Long,
    @ColumnInfo(name = "code_name")
    @SerializedName("name")
    val codename: String,
    @ColumnInfo(name = "close_approach_date")
    @SerializedName("close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name = "absolute_magnitude")
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean,
    @SerializedName("nasa_jpl_url")
    @ColumnInfo(name = "image_url")
    val url: String?,
    @ColumnInfo(name = "created_date")
    val createdDate: String?
    )
