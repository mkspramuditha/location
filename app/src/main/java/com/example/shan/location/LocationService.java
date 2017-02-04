package com.example.shan.location;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import java.text.DateFormat;
import java.util.Date;


public class LocationService extends Service {


    LocationManager locationManager;
    LocationDB locationDB;


    public LocationService() {
        locationDB=LocationDB.getInstance(this);
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean location_enabled=false;
        try{
            location_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception e){}

        if(!location_enabled){
            Toast.makeText(this,"Please enable the location...!",Toast.LENGTH_SHORT).show();
        }
        else {
            String current_time = DateFormat.getDateTimeInstance().format(new Date());
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        catch (SecurityException e){
            Toast.makeText(this,"Location permissions need to be granted...!",Toast.LENGTH_SHORT).show();
        }
            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    locationDB.addLocation(location, current_time);
                } else {
                    Toast.makeText(this, "Cannot find location...!", Toast.LENGTH_LONG).show();
                }
            }
            catch (SecurityException e){
                Toast.makeText(this,"Location permissions need to be granted...!",Toast.LENGTH_SHORT).show();
            }
        }
        showNotification();
        return START_STICKY;

    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, LocationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, LocationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, LocationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Route Radar Tracker")
                .setTicker("TutorialsFace Music Player")
                .setContentText("Tracking...")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);

    }


    @Override
    public void onDestroy() {
        Toast.makeText(this,"Location service stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
