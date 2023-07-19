package com.uwud.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations ORDER BY date DESC")
    fun getLocations(): LiveData<List<LocationEntity>>

    @Insert
    fun addLocation(location: LocationEntity)

    @Insert
    fun addLocations(locations: List<LocationEntity>)

    @Query("DELETE FROM locations")
    fun deleteAllLocations()
}