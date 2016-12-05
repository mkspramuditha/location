package com.example.shan.location;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    MyLocationListener locationListener;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.txtView);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener(this,textView);

    }

    public void clickk(View view){
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                0,
                locationListener);
        if(locationListener.getLoc()!=null)
            textView.setText("LAt:"+locationListener.getLoc().getLatitude());
    }
}
