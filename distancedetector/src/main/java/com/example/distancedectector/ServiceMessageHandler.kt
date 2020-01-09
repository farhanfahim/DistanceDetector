package com.example.distancedectector

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.distancedectector.LocationConstants.Companion.INTENT_KEY_LATITUDE
import com.example.distancedectector.LocationConstants.Companion.INTENT_KEY_LONGITUDE
import com.example.distancedectector.LocationConstants.Companion.LIVE_LOCATION_BROADCAST_CHANNEL

class ServiceMessageHandler {

    companion object {

        fun broadcastingData(context: Context, lat: Double, lng: Double) {
            val intent = Intent(LIVE_LOCATION_BROADCAST_CHANNEL)
            intent.putExtra(INTENT_KEY_LATITUDE, lat)
            intent.putExtra(INTENT_KEY_LONGITUDE, lng)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}