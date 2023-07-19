package com.uwud.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val speed: Float = 0.0F,
    val date: Date = Date()
){
    override fun toString(): String {
        return "$latitude:$longitude@$speed-$date"
    }
}
