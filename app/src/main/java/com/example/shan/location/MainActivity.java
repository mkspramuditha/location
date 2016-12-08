package com.example.shan.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    MyLocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.txtView);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String currentLocation = ""
                                + location.getLatitude()
                                + " ,"
                                + location.getLongitude();

                        Toast.makeText(MainActivity.this, currentLocation, Toast.LENGTH_SHORT).show();
                        textView.setText(currentLocation);


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

            //locationListener=new MyLocationListener(this);
    }

    public void clickk(View view) {

        //textView.setText("lat:"+locationListener.getLatitude()+"\n"+"lon:"+locationListener.getLongitude());
        System.exit(0);
    }


}
