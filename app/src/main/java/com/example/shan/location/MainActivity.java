package com.example.shan.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.shan.location.DB.LocationDB;

public class MainActivity extends AppCompatActivity {

    TextView txtRecords;
    TextView textView;
    LocationManager locationManager;
    MyLocationListener locationListener;

    LocationDB locationDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.txtView);
        txtRecords=(TextView)findViewById(R.id.txtRecords);

        locationDB=new LocationDB(MainActivity.this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0,
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
    }

}
