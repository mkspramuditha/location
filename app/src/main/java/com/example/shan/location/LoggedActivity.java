package com.example.shan.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import java.util.Calendar;

public class LoggedActivity extends AppCompatActivity {

    LocationDB locationDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);


        locationDB= LocationDB.getInstance(LoggedActivity.this);


        String var_username=getIntent().getStringExtra("var_username");
        String var_password=getIntent().getStringExtra("var_password");


        ((TextView)findViewById(R.id.username)).setText(var_username);
        ((TextView)findViewById(R.id.password)).setText(var_password);

//        Start Services
        startLocationService();
    }

    @Override
    public void onBackPressed() {
//        Cleared back button action
//        super.onBackPressed();
    }


    public void startLocationService(){
        setLocationAlarm();
        Toast.makeText(this,"Location service started...",Toast.LENGTH_SHORT).show();
        setMqttAlarm();
    }


    private void setLocationAlarm(){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, LocationService.class);
        PendingIntent pintent = PendingIntent.getService(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 1 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                60000, pintent);
    }

    public void setMqttAlarm(){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, MqttSendService.class);
        PendingIntent pintent = PendingIntent.getService(this, 11, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 1 minute
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                60000, pintent);
    }



}
