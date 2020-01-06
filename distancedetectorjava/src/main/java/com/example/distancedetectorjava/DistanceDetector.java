package com.example.distancedetectorjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class DistanceDetector {

    public static double lat,lng,userDistance;
    public static String status = "";

    boolean checkPreviousLatLng = false;
    public double distance;
    static LocationModel locationModel = new LocationModel();

    static Context context;
    private DistanceListener distanceListener;
    public void setDistanceListener(DistanceListener mSpeedChangeListener) {
        this.distanceListener = mSpeedChangeListener;
    }



    public DistanceDetector(Context context) {
        this.context = context;
    }

    public static double getLatitude(){
        return lat;
    }
    public static double getLongitude(){
        return lng;
    }
    public static double getUserDistance(){
        return userDistance;
    }
    public static void setUserDistance(double uDistance){
        userDistance = uDistance;
    }

    public interface DistanceListener {
        //public void onDistanceChange(double lat, double lng, double distance, String status);
        public void onDistanceChange(String status);
    }

    public double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (earthRadius * c);
    }


    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {

            if(!checkPreviousLatLng){
                locationModel.setpLat(intent.getDoubleExtra(LocationConstants.INTENT_KEY_LATITUDE, 0));
                locationModel.setpLng(intent.getDoubleExtra(LocationConstants.INTENT_KEY_LONGITUDE, 0));

                checkPreviousLatLng = true;

            }else{
                locationModel.setcLat(intent.getDoubleExtra(LocationConstants.INTENT_KEY_LATITUDE, 0));
                locationModel.setcLng(intent.getDoubleExtra(LocationConstants.INTENT_KEY_LONGITUDE, 0));


                distance = distance(locationModel.getpLat(),locationModel.getpLng(),locationModel.getcLat(),locationModel.getcLng());

            }
            lat = intent.getDoubleExtra(LocationConstants.INTENT_KEY_LATITUDE,0);
            lng = intent.getDoubleExtra(LocationConstants.INTENT_KEY_LONGITUDE,0);


            if (distance > userDistance){
                distanceListener.onDistanceChange("out of distance");
            }else{
                //distanceListener.onDistanceChange("safe ");
            }



            }
    };

    public void registerLocationReceiver() {
        LocalBroadcastManager.getInstance(context).registerReceiver(
                locationReceiver, new IntentFilter(LocationConstants.LIVE_LOCATION_BROADCAST_CHANNEL));
    }

    public void unregisterLocationReceiver() {
        try{
            LocalBroadcastManager.getInstance(context).unregisterReceiver(locationReceiver);
        }catch (Exception e){
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public static void startService(Context context) {
        Intent currentLocationService = new Intent(context, CurrentLocationSendingService.class);
        Intent distanceStatus = new Intent(context, DistanceDetectorService.class);
        ContextCompat.startForegroundService(context, distanceStatus);
        ContextCompat.startForegroundService(context, currentLocationService);

    }

    public static void stopService(){
        context.stopService(new Intent(context, CurrentLocationSendingService.class));
        context.stopService(new Intent(context, DistanceDetectorService.class));
    }


}


