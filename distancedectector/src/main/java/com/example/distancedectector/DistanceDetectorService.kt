package com.example.distancedectector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.distancedectector.LocationConstants.Companion.CHANNEL_ID
import com.example.distancedectector.LocationConstants.Companion.INTENT_KEY_STATUS

class DistanceDetectorService : Service(),DistanceDetector.DistanceListener {



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private lateinit var distanceDetector: DistanceDetector

    override fun onCreate() {
        super.onCreate()
        distanceDetector = DistanceDetector(applicationContext)
        distanceDetector.setDistanceListener(this)
        distanceDetector.registerLocationReceiver(applicationContext)
        startInForeground()
    }


    override fun onDestroy() {
        super.onDestroy()
        distanceDetector.unregisterLocationReceiver()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startInForeground()


        return START_NOT_STICKY
    }

    private fun startInForeground() {
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
                .setContentTitle("Speed Detector")
                .setContentText("Speed Detector service started")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1, notification)

        }
    }

    override fun onDistanceChange(status: String) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        val intent = Intent(LocationConstants.STATUS_BROADCAST_CHANNEL)
        // You can also include some extra data.
        intent.putExtra(INTENT_KEY_STATUS, status)

        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }
}