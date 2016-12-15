package com.example.shan.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;


/**
 * Created by Chamod on 12/15/2016.
 */
public class LocationService extends IntentService {

    LocationManager locationManager;
    LocationDB locationDB;


    public LocationService() {
        super("LocationService");
        locationDB=LocationDB.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Location service starting", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,

                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String currentLocation = ""
                                + location.getLatitude()
                                + " ,"
                                + location.getLongitude();

                        //Toast.makeText(MainActivity.this, currentLocation, Toast.LENGTH_SHORT).show();
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
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Location service stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
