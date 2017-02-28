package com.example.shan.location;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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

import android.util.Log;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import java.text.DateFormat;
import java.util.Date;


public class LocationService extends Service {



    LocationDB locationDB;
    LocationTracker locationTracker;


    public LocationService() {
        locationDB=LocationDB.getInstance(this);
    }





    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        locationTracker=new LocationTracker(this);

        Location location = locationTracker.getLocation();
        if (location != null) {
            String current_time = DateFormat.getDateTimeInstance().format(new Date());
            locationDB.addLocation(location, current_time);
        } else {
            Toast.makeText(this, "Cannot find location...!", Toast.LENGTH_LONG).show();
        }
        showNotification();



//      setting alarm again
        Intent location_intent = new Intent(this, LocationService.class);
        PendingIntent location_pending_intent = PendingIntent.getService(this, 0, location_intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 1 minute
        alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+LoggedActivity.REPEAT_TIME, location_pending_intent);
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Route Radar Tracker")
                .setTicker("Route Radar Tracker")
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
