package com.example.shan.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

public class MqttSendService extends Service {

    private static MqttClient client;
    String payload;
    LocationDB locationDB;

    public MqttSendService() {
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        get logged user
//        User user=locationDB.getLoggedUser();


//        retrieve msges from db
        for (LocationRecord lr:locationDB.getPendingLocationRecords()) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("imie", "1234");                       //user.getEmi()
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
            locationDB.sentToServer(lr.getRecord_id());
            Toast.makeText(this,"sent to server",Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }
}
