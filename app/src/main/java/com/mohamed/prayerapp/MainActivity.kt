package com.mohamed.prayerapp

import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.app.base.BaseActivity
import com.mohamed.prayerapp.ui.prayer.PrayersTimeViewModel
import com.mohamed.prayerapp.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    init {

    }


    private val viewModel: PrayersTimeViewModel by viewModels()
    private val locationHelper: LocationHelper by lazy {
        LocationHelper(this) {
            if (it != null && it.first != 0.0 && it.second != 0.0)
                viewModel.getPrayersTime(it.first, it.second)
        }
    }

    override fun onStart() {
        super.onStart()

        checkRequest()

    }

    private fun checkRequest() {
        // Check location permission and request if not granted
        if (!locationHelper.checkLocationPermission()) {
            locationHelper.requestLocationPermission()
        } else {
            // Location permission is already granted, proceed to check GPS status
            locationHelper.checkGpsStatus()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationHelper.GPS_SETTINGS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // GPS was enabled by the user, proceed to get current location
                locationHelper.getCurrentLocation()
            } else {
//                locationHelper.openGpsSettings()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}