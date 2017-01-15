package com.example.shan.location;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class LocationService extends IntentService {

    public static boolean IS_SERVICE_RUNNING = false;

    LocationManager locationManager;
    LocationDB locationDB;

    Context vm;

    private static MqttClient client;
    String payload;

    public LocationService() {
        super("LocationService");
        locationDB=LocationDB.getInstance(this);

        try {
            MemoryPersistence persistance = new MemoryPersistence();
            client = new MqttClient("tcp://128.199.217.137:1883", "client1", persistance);
            client.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        vm=this;

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationDB.addLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        payload = "test";

        Toast.makeText(vm,payload,Toast.LENGTH_LONG).show();

        //get logged user
        User user=locationDB.getLoggedUser();

        //retrieve msges from db
        for (LocationRecord lr:locationDB.getPendingLocationRecords()) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("imie", user.getEmi());
                jsonObject.put("latitude",lr.getLatitude());
                jsonObject.put("longitude",lr.getLongitude());
                jsonObject.put("time",lr.getUpdated_time());
            }
            catch (JSONException e){}
            payload=jsonObject.toString();

            //send mqqtt msg to server
            MqttMessage message = new MqttMessage(payload.getBytes());
            try {
                client.publish("test", message);
            } catch (MqttPersistenceException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }

        showNotification();
        return START_STICKY;

    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
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
                .setContentTitle("TutorialsFace Music Player")
                .setTicker("TutorialsFace Music Player")
                .setContentText("My song")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous",
                        ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "Play",
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next",
                        pnextIntent).build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);

    }


    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Location service stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
