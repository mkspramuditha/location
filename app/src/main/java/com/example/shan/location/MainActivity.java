package com.example.shan.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.shan.location.DB.LocationDB;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import static android.R.id.message;

public class MainActivity extends AppCompatActivity {

    TextView txtRecords;
    TextView textView;
    LocationManager locationManager;
    MyLocationListener locationListener;

    LocationDB locationDB;
    MqttAndroidClient mqttAndroidClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.txtView);
        txtRecords=(TextView)findViewById(R.id.txtRecords);

        locationDB=new LocationDB(MainActivity.this);


        mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), "tcp://128.199.217.137:1883", "androidSampleClient");
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message Arrived!: " + topic + ": " + new String(message.getPayload()));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery Complete!");
            }
        });


        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Connection Success!");
                    try {
                        System.out.println("Subscribing to /test");
                        mqttAndroidClient.subscribe("test", 0);
                        System.out.println("Subscribed to /test");
                        System.out.println("Publishing message..");
                        mqttAndroidClient.publish("test", new MqttMessage("Hello world!".getBytes()));
                    } catch (MqttException ex) {

                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Connection Failure!");
                }
            });
        } catch (MqttException ex) {

        }
















        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String currentLocation = ""
                                + location.getLatitude()
                                + " ,"
                                + location.getLongitude();

                        //Toast.makeText(MainActivity.this, currentLocation, Toast.LENGTH_SHORT).show();
                        textView.setText(currentLocation);
                        locationDB.addLocation(location);

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

    public void clickk(View view) {

        System.exit(0);
    }


    public void clickShowRecords(View view){
        txtRecords.setText("");
        for(LocationRecord r:locationDB.getAllLocationRecords()){
            txtRecords.setText(txtRecords.getText()+"\n"+"lat:"+r.getLatitude()+", lon:"+r.getLongitude()+
            "time:"+r.getUpdated_time()+",user_id:"+r.getUser_id());

        }

        try {
            String text = txtRecords.getText().toString();
            mqttAndroidClient.publish("test", new MqttMessage(text.getBytes()));
        } catch (MqttException ex) {

        }
    }



}
