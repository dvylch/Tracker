package com.uwud.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "devices",
    foreignKeys = [
        ForeignKey(entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationID"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class ScannedDeviceEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val locationID: UUID,
    val rssi: Double?,
    val companyId: String?,
    val manufacturer: String?
)
