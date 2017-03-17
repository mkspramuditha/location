package com.example.shan.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.shan.location.DB.LocationDB;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by chamod on 2/28/17.
 */

public class LocationTracker implements LocationListener{
    Context context;

    private static final long MIN_DISTANCE = 0,MIN_TIME=5*60*1000;

    LocationManager locationManager;
    Location location=null;

    boolean isGPSEnabled=false,isNetworkEnabled=false;

    public LocationTracker(Context context) {
        this.context=context;
        getLocation();
    }

    public Location getLocation(){
        locationManager = (LocationManager)  context.getSystemService(Context.LOCATION_SERVICE);

        try{
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception e){
            Log.e("error:",e.toString());
        }

        if(!isGPSEnabled && !isNetworkEnabled){
            Toast.makeText(context,"Please enable the location...!",Toast.LENGTH_SHORT).show();
        }
        else if(isGPSEnabled){
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

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
            catch (SecurityException e){
                Toast.makeText(context,"Location permissions need to be granted...!",Toast.LENGTH_SHORT).show();
            }
            try {
                 location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return  location;
            }
            catch (SecurityException e){
                Toast.makeText(context,"Location permissions need to be granted...!",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

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
            catch (SecurityException e){
                Toast.makeText(context,"Location permissions need to be granted...!",Toast.LENGTH_SHORT).show();
            }
            try {
                 location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return location;
            }
            catch (SecurityException e){
                Toast.makeText(context,"Location permissions need to be granted...!",Toast.LENGTH_SHORT).show();
            }
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
