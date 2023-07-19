package com.uwud

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.uwud.db.LocationEntity
import com.uwud.db.LocationRepository
import java.util.concurrent.Executors

class MainVM(application: Application): AndroidViewModel(application) {

    var location: String by mutableStateOf("")
    var speed: Float by mutableStateOf(0.0F)
    var status: String by mutableStateOf("Not running")

    private val locationRepository = LocationRepository.getInstance(application.applicationContext,Executors.newSingleThreadExecutor())

    val locationListLiveData = locationRepository.getLocations()

    fun updateLocation(locationEntities: List<LocationEntity>) {
        if(!locationEntities.isNullOrEmpty()){
            val data = locationEntities[0]
            location = "${data.latitude} - ${data.longitude} | ${data.date}"
            speed = data.speed
        } else {
            location = "Error"
            speed = Float.NaN
        }
    }

    fun startLocationUpdates(){
        status = "Running"
        locationRepository.startLocationUpdates()
    }

    fun stopLocationUpdates(){
        status = "Not running"
        locationRepository.stopLocationUpdates()
    }



}