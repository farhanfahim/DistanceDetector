package com.example.distancedectector

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class RunTimePermissions {

    companion object {
    // Storage Permissions variables
    private val REQUEST_CODE = 6361
    private val PERMISSIONS =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)


    //persmission method.

        fun verifyStoragePermissions(activity: Activity) {
            // Check if we have read or write permission
            val readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writePermission =
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            val locationPermission =
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarsePermission =
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            val internetPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET)
            val wifiPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE)
            val readPhonePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)

            if (locationPermission != PackageManager.PERMISSION_GRANTED || coarsePermission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_CODE
                )
            }
        }
    }
}