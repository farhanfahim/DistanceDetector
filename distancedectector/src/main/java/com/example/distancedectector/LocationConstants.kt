package com.example.distancedectector

class LocationConstants {
    companion object {
        val CHANNEL_ID = "com.github.farhanfahim.CurrentLocationServiceChannel"
        val LIVE_LOCATION_BROADCAST_CHANNEL = "com.github.farhanfahim.LocationBroadCast"
        val STATUS_BROADCAST_CHANNEL = "com.github.farhanfahim.statusBroadcast"
        val MIN_TIME_BW_UPDATES: Long = 5000
        val MIN_DISTANCE_BW_UPDATES: Long = 0
        val INTENT_KEY_USER_ID = "user_id_key"
        val INTENT_KEY_EVENT_ID = "event_id_key"
        val INTENT_KEY_NOTIFICATION_TITLE = "title_key"
        val INTENT_KEY_NOTIFICATION_DETAIL = "detail_key"
        val INTENT_KEY_LATITUDE = "latitude"
        val INTENT_KEY_RESULT = "result"
        val INTENT_KEY_STATUS = "status"
        val INTENT_KEY_LONGITUDE = "longitude"
        val EVENT_FROM_GPS = 1
    }
}