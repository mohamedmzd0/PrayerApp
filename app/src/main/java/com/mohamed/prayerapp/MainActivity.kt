package com.mohamed.prayerapp

import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.app.base.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mohamed.prayerapp.ui.prayer.PrayersTimeViewModel
import com.mohamed.prayerapp.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    init {

    }

    override fun onStart() {
        super.onStart()
        checkRequest()


    }


    private val viewModel: PrayersTimeViewModel by viewModels()
    private val locationHelper: LocationHelper by lazy {
        LocationHelper(
            activity = this,
            onLocationReceived = ::sendRequest,
            showPermissionDeniedDialog = ::showPermissionDeniedDialog
        )
    }


    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.permission_denied_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.retry)) { dialog, _ ->
                dialog.dismiss()
                checkRequest()
            }
            .setCancelable(false)
            .create().show()
    }

    private fun sendRequest(pair: Pair<Double, Double>?) {
//        if (pair != null && pair.first != 0.0 && pair.second != 0.0)
        viewModel.getPrayersTime(pair?.first ?: 0.0, pair?.second ?: 0.0)
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}