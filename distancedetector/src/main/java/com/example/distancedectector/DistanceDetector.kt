package com.example.distancedectector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.ArrayList

class DistanceDetector(applicationContext: Context) {
    companion object {
        private var lat: Double = 0.0
        private var lng: Double = 0.0
        private var userDistance: Double = 0.0
        //private var context: Context? = null


        fun getLatitude(): Double {
            return lat
        }

        fun getLongitude(): Double {
            return lng
        }

        fun getUserDistance(): Double {
            return userDistance
        }

        fun setUserDistance(uDistance: Double) {
            userDistance = uDistance
        }

        fun startService(context: Context) {
            val currentLocationService = Intent(context, CurrentLocationSendingService::class.java)
            val distanceStatus = Intent(context, DistanceDetectorService::class.java)
            ContextCompat.startForegroundService(context, distanceStatus)
            ContextCompat.startForegroundService(context, currentLocationService)
            registerStatusReceiver(context)

        }

        fun stopService(context: Context) {
            context.stopService(Intent(context, CurrentLocationSendingService::class.java))
            context.stopService(Intent(context, DistanceDetectorService::class.java))
            unregisterStatusReceiver(context)

        }

        private var statusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                //status = intent.getStringExtra(LocationConstants.INTENT_KEY_STATUS)
            }
        }

        private fun registerStatusReceiver(context: Context) {
            LocalBroadcastManager.getInstance(context).registerReceiver(
                statusReceiver, IntentFilter(LocationConstants.LIVE_LOCATION_BROADCAST_CHANNEL)
            )

        }

        private fun unregisterStatusReceiver(context: Context) {

            LocalBroadcastManager.getInstance(context).unregisterReceiver(statusReceiver)

        }
    }

    private var checkPreviousLatLng = false
    var distance: Double = 0.toDouble()
    private var locationModel = LocationModel()

    private lateinit var context: Context
    private lateinit var distanceListener: DistanceListener


    fun setDistanceListener(mSpeedChangeListener: DistanceListener) {
        this.distanceListener = mSpeedChangeListener
    }


    interface DistanceListener {
        //public void onDistanceChange(double lat, double lng, double distance, String status);
        fun onDistanceChange(status: String)

    }


    fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }


    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (!checkPreviousLatLng) {
                locationModel.pLat = (intent.getDoubleExtra(LocationConstants.INTENT_KEY_LATITUDE, 0.0))
                locationModel.pLng = (intent.getDoubleExtra(LocationConstants.INTENT_KEY_LONGITUDE, 0.0))

                checkPreviousLatLng = true

            } else {
                locationModel.cLat = (intent.getDoubleExtra(LocationConstants.INTENT_KEY_LATITUDE, 0.0))
                locationModel.cLng = (intent.getDoubleExtra(LocationConstants.INTENT_KEY_LONGITUDE, 0.0))



                distance = distance(
                    locationModel.pLat,
                    locationModel.pLng,
                    locationModel.cLat,
                    locationModel.cLng
                )
            }
            lat = intent.getDoubleExtra(LocationConstants.INTENT_KEY_LATITUDE, 0.0)
            lng = intent.getDoubleExtra(LocationConstants.INTENT_KEY_LONGITUDE, 0.0)
            //            lat = locationModel.getcLat();
            //            lng = locationModel.getcLng();

            if (distance > userDistance) {
                distanceListener.onDistanceChange("out of limit $distance")
            }/* else {
                distanceListener.onDistanceChange("safe $distance")
            }*/


        }
    }

    fun registerLocationReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            locationReceiver, IntentFilter(LocationConstants.LIVE_LOCATION_BROADCAST_CHANNEL)
        )

    }

    fun unregisterLocationReceiver() {
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(locationReceiver)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }


}