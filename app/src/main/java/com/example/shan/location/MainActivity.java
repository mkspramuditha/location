package com.example.shan.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shan.location.DB.LocationDB;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MainActivity extends AppCompatActivity {

    TextView txtRecords;
    TextView textView;

    LocationDB locationDB;
    MqttAndroidClient mqttAndroidClient;

    Intent locationServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtRecords=(TextView)findViewById(R.id.txtRecords);

        locationDB=LocationDB.getInstance(MainActivity.this);


       /* mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), "tcp://128.199.217.137:1883",
                "androidSampleClient");
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

        }*/

        //Toast.makeText(this,"before service",Toast.LENGTH_LONG).show();


    }

    public void clickedStartService(View view){
        locationServiceIntent=new Intent(this,LocationService.class);
        Button button = (Button) view;
        if (!LocationService.IS_SERVICE_RUNNING) {
            locationServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            LocationService.IS_SERVICE_RUNNING = true;
            button.setText("Stop Service");
        } else {
            locationServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            LocationService.IS_SERVICE_RUNNING = false;
            button.setText("Start Service");

        }
        startService(locationServiceIntent);
    }


    public void clickedStopService(View view){
        stopService(locationServiceIntent);
    }

    public void clickedExitApp(View view) {
        System.exit(0);
    }


    public void clickShowRecords(View view){
        txtRecords.setText("");
        for(LocationRecord r:locationDB.getAllLocationRecords()){
            txtRecords.setText(txtRecords.getText()+"\n"+"lat:"+r.getLatitude()+", lon:"+r.getLongitude()+
            "time:"+r.getUpdated_time()+",user_id:"+r.getUser_id());

        }

        /*try {
            String text = txtRecords.getText().toString();
            mqttAndroidClient.publish("test", new MqttMessage(text.getBytes()));
        } catch (MqttException ex) {

        }*/
    }



}
