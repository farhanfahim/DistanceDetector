package com.example.distancedetectorjava;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_EVENT_ID;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_LATITUDE;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_LONGITUDE;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_USER_ID;
import static com.example.distancedetectorjava.LocationConstants.LIVE_LOCATION_BROADCAST_CHANNEL;

public class ServiceMessageHandler {

    /**
     *
     * @param context
     * @param lat
     * @param lng
     * @param userId
     * @param eventType  1 for EVENT_FROM_GPS, 2 for EVENT_FROM_FIREBASE
     */
    public static void broadcastingData(Context context, double lat, double lng, String userId, int eventType) {
        Intent intent = new Intent(LIVE_LOCATION_BROADCAST_CHANNEL);
        // You can also include some extra data.
        intent.putExtra(INTENT_KEY_LATITUDE, lat);
        intent.putExtra(INTENT_KEY_LONGITUDE, lng);
        intent.putExtra(INTENT_KEY_USER_ID, userId);
        intent.putExtra(INTENT_KEY_EVENT_ID, eventType);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


}
