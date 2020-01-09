package com.example.distancedectector

class LocationConstants {
    companion object {
        const val CHANNEL_ID = "com.github.farhanfahim.DistanceDetector"
        const val LIVE_LOCATION_BROADCAST_CHANNEL = "com.github.farhanfahim.LocationBroadCast"
        const val STATUS_BROADCAST_CHANNEL = "com.github.farhanfahim.statusBroadcast"
        const val MIN_TIME_BW_UPDATES: Long = 5000
        const val MIN_DISTANCE_BW_UPDATES: Long = 0
        const val INTENT_KEY_NOTIFICATION_TITLE = "title_key"
        const val INTENT_KEY_NOTIFICATION_DETAIL = "detail_key"
        const val INTENT_KEY_LATITUDE = "latitude"
        const val INTENT_KEY_STATUS = "status"
        const val INTENT_KEY_LONGITUDE = "longitude"
    }
}