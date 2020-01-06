package com.example.distancedetectorjava;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import static com.example.distancedetectorjava.LocationConstants.CHANNEL_ID;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_LATITUDE;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_LONGITUDE;
import static com.example.distancedetectorjava.LocationConstants.INTENT_KEY_STATUS;

public class DistanceDetectorService extends Service implements DistanceDetector.DistanceListener {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    DistanceDetector distanceDetector;

    @Override
    public void onCreate() {
        super.onCreate();
        distanceDetector = new DistanceDetector(getApplicationContext());
        distanceDetector.setDistanceListener(this);
        distanceDetector.registerLocationReceiver();
        startInForeground();
    }

    @Override
    public void onDestroy() {
        distanceDetector.unregisterLocationReceiver();
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startInForeground();


        return START_NOT_STICKY;
    }

    private void startInForeground() {
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
                    .setContentTitle("Speed Detector")
                    .setContentText("Speed Detector service started")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);

        }
    }

    @Override
//    public void onDistanceChange(double lat, double lng, double dis, String status) {
//        Toast.makeText(this, String.valueOf(lat+","+lng+","+dis+","+status), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent("com.locationdemo.showdata");
//        // You can also include some extra data.
//        intent.putExtra(INTENT_KEY_LATITUDE, lat);
//        intent.putExtra(INTENT_KEY_LONGITUDE, lng);
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//    }

    public void onDistanceChange(String status) {
        //Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LocationConstants.STATUS_BROADCAST_CHANNEL);
        // You can also include some extra data.
        intent.putExtra(INTENT_KEY_STATUS, status);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
