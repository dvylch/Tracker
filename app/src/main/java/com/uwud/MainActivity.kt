package com.uwud

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uwud.ui.theme.WudTheme


class MainActivity : ComponentActivity() {
    private val vm: MainVM by viewModels()
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WudTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(vm)
                }

            }
        }

        vm.locationListLiveData.observe(this){
            Log.i("MainActivity", "observing size: ${it.size}")
            vm.updateLocation(it)
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        Log.w("myApp", "[#] GPSActivity.java - Check Location Permission...")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("myApp", "[#] GPSActivity.java - Precise Location Permission granted")
            // Permission Granted
        } else {
            Log.w("myApp", "[#] GPSActivity.java - Precise Location Permission denied")
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
            Log.w("myApp", "$showRationale, ${ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) }")
            if (showRationale || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                Log.w(
                    "myApp",
                    "[#] GPSActivity.java - Precise Location Permission denied, need new check"
                )
                val listPermissionsNeeded: MutableList<String> = ArrayList()
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(
                    this,
                    listPermissionsNeeded.toTypedArray<String>(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
        }
    }
}



@Composable
fun MainView(vm: MainVM) {
    Column(modifier = Modifier.padding(start = 20.dp)) {
        Row(modifier = Modifier.padding(top = 50.dp)){
            Text(text = "Status: ")
            ShowStatus(vm)
        }
        Row(modifier = Modifier.padding(top = 50.dp)){
            Text(text = "Location: ")
            Location(vm)
        }

        Row(modifier = Modifier.padding(bottom = 50.dp)){
            Text(text = "Speed: ")
            Speed(vm)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly){
            Button(onClick = {
                vm.startLocationUpdates()
            }) {
                Text(text = "Start")
            }
            Button(onClick = {
                vm.stopLocationUpdates()
            }) {
                Text(text = "Stop")
            }
            Button(onClick = {
            }) {
                Text(text = "Export")
            }
        }

    }
}

@Composable
fun Location(vm: MainVM) {
    Text(text = vm.location)
}

@Composable
fun Speed(vm: MainVM) {
    Text(text = String.format("%.2f km/h", vm.speed))
}

@Composable
fun ShowStatus(vm: MainVM) {
    Text(text = vm.status)
}
