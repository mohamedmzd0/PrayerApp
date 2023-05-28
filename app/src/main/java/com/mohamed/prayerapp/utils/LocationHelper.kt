package com.mohamed.prayerapp.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener

private const val TAG = "LocationHelper"

class LocationHelper(
    private val activity: Activity,
    private val onLocationReceived: (Pair<Double, Double>?) -> Unit
) {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
        const val GPS_SETTINGS_REQUEST_CODE = 101
    }


    private val locationManager: LocationManager by lazy {
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun checkLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun openGpsSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }

    fun getCurrentLocation() {
        if (!checkLocationPermission()) {
            Log.e(TAG, "getCurrentLocation: not permssion")
            onLocationReceived.invoke(null)
            return
        }
        if (!isGpsEnabled()) {
            Log.e(TAG, "getCurrentLocation: not gps")
            onLocationReceived.invoke(null)
            openGpsSettings()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location == null) {
                    requestCurrentLocation()
                } else
                    onLocationReceived(Pair(location.latitude, location.longitude))
            }
            .addOnFailureListener { exception: Exception ->
                onLocationReceived.invoke(null)
                Log.e(TAG, "getCurrentLocation: ", exception)
                // Handle location retrieval failure
            }
    }

    private fun requestCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location: Location? ->
                if (location == null) {
                    onLocationReceived.invoke(null)
                } else
                    onLocationReceived(Pair(location.latitude, location.longitude))
            }
            .addOnFailureListener { exception: Exception ->
                onLocationReceived.invoke(null)
                Log.e(TAG, "getCurrentLocation: ", exception)
                // Handle location retrieval failure
            }

    }

    fun checkGpsStatus() {
        val locationManager =
            activity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is enabled, proceed to get current location
            getCurrentLocation()
        } else {
            openGpsSettings()
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed to check GPS status
                checkGpsStatus()
            } else {
                // Location permission denied
                // Handle the denial or display a message to the user
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GPS_SETTINGS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // GPS was enabled by the user, proceed to get current location
                getCurrentLocation()
            } else {
                // GPS was not enabled by the user
                // Handle the denial or display a message to the user
            }
        }
    }
}
