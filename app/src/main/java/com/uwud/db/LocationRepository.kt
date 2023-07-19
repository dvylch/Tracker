package com.uwud.db

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService

class LocationRepository private constructor(
    private val myDatabase: MyDatabase,
    private val myLocationManager: MyLocationManager,
    private val executor: ExecutorService
) {
    private val locationDao = myDatabase.locationDao()

    fun getLocations(): LiveData<List<LocationEntity>> {
        Log.i("LocationRepo", "GetLocations")
        return locationDao.getLocations()
    }

    fun addLocation(locationEntity: LocationEntity){
        executor.execute {
            locationDao.addLocation(locationEntity)
        }
    }

    fun addLocations(locations: List<LocationEntity>){
        executor.execute {
            Log.i("LocationRepo", "AddLocations")
            locationDao.addLocations(locations)
        }
    }

    // Location related fields/methods:
    /**
     * Status of whether the app is actively subscribed to location changes.
     */
    val receivingLocationUpdates: LiveData<Boolean> = myLocationManager.receivingLocationUpdates

    /**
     * Subscribes to location updates.
     */
    @MainThread
    fun startLocationUpdates() = myLocationManager.startLocationUpdates()

    /**
     * Un-subscribes from location updates.
     */
    @MainThread
    fun stopLocationUpdates() = myLocationManager.stopLocationUpdates()

    companion object {
        @Volatile private var INSTANCE: LocationRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): LocationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationRepository(
                    MyDatabase.getInstance(context),
                    MyLocationManager.getInstance(context),
                    executor)
                    .also { INSTANCE = it }
            }
        }
    }
}