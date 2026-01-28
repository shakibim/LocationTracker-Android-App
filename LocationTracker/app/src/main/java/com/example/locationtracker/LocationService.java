package com.example.locationtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService extends Service {

    private static final String CHANNEL_ID = "LocationServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        database = new LocationDatabaseHelper(this).getWritableDatabase();

        createNotificationChannel();
        startForegroundService();
        startLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startForegroundService() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Tracking Active")
                .setContentText("Tracking location every 5 minutes")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setOngoing(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                    NOTIFICATION_ID,
                    notification,
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            );
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    private void startLocationUpdates() {

        com.google.android.gms.location.LocationRequest request =
                new com.google.android.gms.location.LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        300_000
                )
                        .setMinUpdateIntervalMillis(300_000)
                        .setWaitForAccurateLocation(false)
                        .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location location = result.getLastLocation();
                if (location != null) {
                    saveLocation(location);

                    Toast.makeText(
                            LocationService.this,
                            "Lat: " + location.getLatitude()
                                    + ", Lon: " + location.getLongitude(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                getMainLooper()
        );
    }

    private void saveLocation(Location location) {

        ContentValues values = new ContentValues();
        values.put(LocationDatabaseHelper.COL_LAT,
                Math.round(location.getLatitude() * 1e6) / 1e6);
        values.put(LocationDatabaseHelper.COL_LON,
                Math.round(location.getLongitude() * 1e6) / 1e6);
        values.put(LocationDatabaseHelper.COL_TIME,
                System.currentTimeMillis());

        database.insert(LocationDatabaseHelper.TABLE_NAME, null, values);
        Log.d("LocationService", "Location saved");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            getSystemService(NotificationManager.class)
                    .createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        database.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
