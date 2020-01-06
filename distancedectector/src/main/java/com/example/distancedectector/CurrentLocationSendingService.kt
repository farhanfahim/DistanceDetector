package com.example.distancedectector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.distancedectector.LocationConstants.Companion.CHANNEL_ID
import com.example.distancedectector.LocationConstants.Companion.INTENT_KEY_NOTIFICATION_DETAIL
import com.example.distancedectector.LocationConstants.Companion.INTENT_KEY_NOTIFICATION_TITLE
import com.example.distancedectector.ServiceMessageHandler.Companion.broadcastingData

class CurrentLocationSendingService : Service(),LocationListener{


    private lateinit var mContext: Context
    private var isGPSEnabled = false

    private var isNetworkEnabled = false
    private var canGetLocation = false

    private var location: Location? = null
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var title:String? = ""
    private var details:String? = ""
    private var locationHandler = Handler()
    private var mlocationManager: LocationManager? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.mContext = this@CurrentLocationSendingService
        title = intent!!.getStringExtra(INTENT_KEY_NOTIFICATION_TITLE)
        details = intent.getStringExtra(INTENT_KEY_NOTIFICATION_DETAIL)
        //locationHandler.postDelayed(getLocationRunnable,0)
        getLocation()
        createNotificationChannel()

        return START_NOT_STICKY
    }

    /*private var getLocationRunnable: Runnable = object : Runnable {
        override fun run() {
            val location = getLocation()
            if (location != null) {
                Toast.makeText(mContext,""+location.latitude,Toast.LENGTH_SHORT).show()
                broadcastingData(mContext, location.latitude,location.longitude)
            }else{
                broadcastingData(mContext, 0.0,0.0)
            }
            locationHandler.postDelayed(this, 1000)
        }
    }*/

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    fun getLocation(): Location? {
        try {
            mlocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = mlocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = mlocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    mlocationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        LocationConstants.MIN_TIME_BW_UPDATES,
                        LocationConstants.MIN_DISTANCE_BW_UPDATES.toFloat(),
                        this
                    )
                    Log.d("Network", "Network")
                    if (mlocationManager != null) {

                        location = mlocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            lat = location!!.latitude
                            lng = location!!.longitude

                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {

                        mlocationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            LocationConstants.MIN_TIME_BW_UPDATES,
                            LocationConstants.MIN_DISTANCE_BW_UPDATES.toFloat(),
                            this
                        )
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (mlocationManager != null) {

                            location = mlocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                lat = location!!.latitude
                                lng = location!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    fun stopUsingGPS() {
        if (mlocationManager != null) {
            mlocationManager!!.removeUpdates(this@CurrentLocationSendingService)
        }
    }

    fun getLatitude(): Double {
        if (location != null) {
            lat = location!!.latitude
        }


        return lat
    }

    fun getLongitude(): Double {
        if (location != null) {
            lng = location!!.longitude
        }

        return lng
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
            val notificationIntent = Intent(this, Context::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
            )

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(details)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1, notification)
        }
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location?) {
        this.location = location

        val latitude = getLatitude()
        val longitude = getLongitude()

        //Toast.makeText(this,""+latitude +longitude,Toast.LENGTH_SHORT).show()
        broadcastingData(mContext, latitude, longitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    override fun onDestroy() {
        super.onDestroy()
        //locationHandler.removeCallbacks(getLocationRunnable)
        stopUsingGPS()
    }




}