package com.example.shan.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private static MqttClient client;


    LocationDB locationDB;
    LocationTracker locationTracker;


    public AlarmReceiver() {

////        Mqtt object................................
//        try {
//            MemoryPersistence persistance = new MemoryPersistence();
//            client = new MqttClient("tcp://128.199.217.137:1883", "client1", persistance);
//            client.connect();
//
//        } catch (MqttException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //......................Mqtt send service
    private void sendMqttMsg(Context context){
        try {
            MemoryPersistence persistance = new MemoryPersistence();
            client = new MqttClient("tcp://128.199.217.137:1883", "client1", persistance);

            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        if(!isInternetConnected(context)){
            Toast.makeText(context,"Please enable data or wifi...!",Toast.LENGTH_SHORT).show();
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
                MqttMessage message = new MqttMessage(payload.getBytes());
                try {
                    client.publish("test", message);
                } catch (MqttPersistenceException e) {
                    e.printStackTrace();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                locationDB.sentToServer(lr.getRecord_id());
                Toast.makeText(context,"sent to server",Toast.LENGTH_SHORT).show();
                try {
                    client.disconnect();
                }
                catch (MqttException e){
                    e.printStackTrace();
                }
            }
        }
    }



    private boolean isInternetConnected (Context context) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) context
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


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        locationDB= LocationDB.getInstance(context);

        locationTracker=new LocationTracker(context);

        Location location = locationTracker.getLocation();
        if (location != null) {
            String current_time = DateFormat.getDateTimeInstance().format(new Date());
            locationDB.addLocation(location, current_time);
        } else {
            Toast.makeText(context, "Cannot find location...!", Toast.LENGTH_LONG).show();
        }
//...................MQTT send service..............................................

        sendMqttMsg(context);


//      setting alarm again

        Intent intent2 = new Intent(context, AlarmReceiver.class);

        PendingIntent location_pending_intent = PendingIntent.getBroadcast(context.getApplicationContext(),0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Start service every 5 minute
        alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+LoggedActivity.REPEAT_TIME, location_pending_intent);
    }
}
