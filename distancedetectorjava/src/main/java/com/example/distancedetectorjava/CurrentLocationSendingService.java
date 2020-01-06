package com.example.distancedetectorjava;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import static com.example.distancedetectorjava.LocationConstants.CHANNEL_ID;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_NOTIFICATION_DETAIL;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_NOTIFICATION_TITLE;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_USER_ID;
import static com.example.distancedetectorjava.ServiceMessageHandler.broadcastingData;


public class CurrentLocationSendingService extends Service implements LocationListener {

   private Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;//Location
    double latitude;//Latitude
    double longitude;//Longitude
    String userId = "", title = "", details = "";

    // The minimum time between updates in milliseconds

    // Declaring a Location Manager
    protected LocationManager mlocationManager;


    public CurrentLocationSendingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mContext = CurrentLocationSendingService.this;
        userId = intent.getStringExtra(INTENT_KEY_USER_ID);
        title = intent.getStringExtra(INTENT_KEY_NOTIFICATION_TITLE);
        details = intent.getStringExtra(INTENT_KEY_NOTIFICATION_DETAIL);
        //locationHandler.postDelayed(getLocationRunnable,0);
        getLocation();
        createNotificationChannel();


        //do heavy work on a background thread


        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //getLocation();
        createNotificationChannel();
    }

//    Handler locationHandler = new Handler();
//    Runnable getLocationRunnable =new Runnable() {
//        @Override
//        public void run() {
//            Location location = getLocation();
//            broadcastingData(mContext, location.getLatitude(), location.getLongitude(), userId, LocationConstants.EVENT_FROM_GPS);
//            //Toast.makeText(mContext,""+location.getLatitude(),Toast.LENGTH_LONG).show();
//            locationHandler.postDelayed(getLocationRunnable,2000);
//        }
//    };

    public Location getLocation() {
        try {
            mlocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //noinspection MissingPermission
                    mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LocationConstants.MIN_TIME_BW_UPDATES, LocationConstants.MIN_DISTANCE_BW_UPDATES, this);
                    Log.d("Network", "Network");
                    if (mlocationManager != null) {
                        //noinspection MissingPermission
                        location = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        //noinspection MissingPermission
                        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LocationConstants.MIN_TIME_BW_UPDATES, LocationConstants.MIN_DISTANCE_BW_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mlocationManager != null) {
                            //noinspection MissingPermission
                            location = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if (mlocationManager != null) {
            mlocationManager.removeUpdates(CurrentLocationSendingService.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }


        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        double latitude = getLatitude();
        double longitude = getLongitude();

        broadcastingData(mContext, latitude, longitude, userId, LocationConstants.EVENT_FROM_GPS);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            Intent notificationIntent = new Intent(this, Context.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(details)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
        }
    }

    public static boolean isLocationEnabled(final Activity activity) {

        try {
            LocationManager mlocationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setTitle("GPS is settings");

                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public void onDestroy() {
        stopUsingGPS();
        //locationHandler.removeCallbacks(getLocationRunnable);
        super.onDestroy();
    }
}
