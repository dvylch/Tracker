package com.uwud

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import com.uwud.db.LocationEntity
import com.uwud.db.LocationRepository
import java.util.Date
import java.util.concurrent.Executors

private const val TAG = "LUBroadcastReceiver"

class LocationUpdatesBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_PROCESS_UPDATES) {

            // Checks for location availability changes.
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->
                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "Location services are no longer available!")
                }
            }

            LocationResult.extractResult(intent)?.let { locationResult ->
                val locations = locationResult.locations.map { location ->
                    Log.d(TAG, "Location: $location")
                    LocationEntity(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        speed = location.speed,
                        date = Date(location.time)
                    )
                }
                Log.d(TAG, "Locations: $locations")
                if (locations.isNotEmpty()) {
                    LocationRepository.getInstance(context, Executors.newSingleThreadExecutor())
                        .addLocations(locations)
                }
            }
        }
    }
    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.uwud.locationupdatesbackgroundkotlin.action." +
                    "PROCESS_UPDATES"
    }
}