package com.example.shan.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        LocationDB locationDB = LocationDB.getInstance(this);

//        Check whether logged
        User loggedUser = locationDB.getLoggedUser();
        if (loggedUser != null) {
            ((TextView) findViewById(R.id.username)).setText(loggedUser.getUsername());
            ((TextView) findViewById(R.id.password)).setText(loggedUser.getPassword());
        }
    }
}
