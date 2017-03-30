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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import android.util.Log;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;


public class LocationService extends Service {

//    private static MqttClient client;


    LocationDB locationDB;
    LocationTracker locationTracker;


    public LocationService() {

        locationDB=LocationDB.getInstance(this);

//        Mqtt object................................
//        try {
//            MemoryPersistence persistance = new MemoryPersistence();
//            client = new MqttClient("tcp://128.199.217.137:1883", "client1", persistance);
////            client.connect();
//
//        } catch (MqttException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//......................Mqtt send service
    private void sendMqttMsg(){
//        try {
////            MemoryPersistence persistance = new MemoryPersistence();
////            client = new MqttClient("tcp://128.199.217.137:1883", "client1", persistance);
//            client.connect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }

        if(!isInternetConnected()){
            Toast.makeText(this,"Please enable data or wifi...!",Toast.LENGTH_SHORT).show();
        }
        else{
            //        retrieve msges from db
            for (LocationRecord lr:locationDB.getPendingLocationRecords()) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("imie", lr.getEmi_no());
                    jsonObject.put("latitude",lr.getLatitude());
                    jsonObject.put("longitude",lr.getLongitude());
                    jsonObject.put("time",lr.getUpdated_time());
                }
                catch (JSONException e){}
                String payload=jsonObject.toString();

                //send mqqtt msg to server
//                MqttMessage message = new MqttMessage(payload.getBytes());
//                try {
//                    client.publish("test", message);
//                } catch (MqttPersistenceException e) {
//                    e.printStackTrace();
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
                locationDB.sentToServer(lr.getRecord_id());
                Toast.makeText(this,"sent to server",Toast.LENGTH_SHORT).show();
            }
        }
//        try {
//            client.disconnect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
    }



    private boolean isInternetConnected () {
        ConnectivityManager connectivityMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }

//    ....................................Location service.................................................

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
//...................MQTT send service..............................................

        sendMqttMsg();


//      setting alarm again
//        Intent location_intent = new Intent(this, LocationService.class);
//        PendingIntent location_pending_intent = PendingIntent.getService(this, 0, location_intent, 0);
//
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        // Start service every 5 minute
//        alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+LoggedActivity.REPEAT_TIME, location_pending_intent);
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
                .setSmallIcon(R.drawable.logo)
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
