package com.example.shan.location;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    LocationManager locationManager;
    MyLocationListener locationListener;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.txtView);

        locationListener=new MyLocationListener(this);


    }

    public void clickk(View view) {

        textView.setText("lat:"+locationListener.getLatitude()+"\n"+"lon:"+locationListener.getLongitude());
    }


}
