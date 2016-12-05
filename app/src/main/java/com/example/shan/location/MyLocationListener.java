package com.example.shan.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chamod on 12/4/2016.
 */
public class MyLocationListener implements LocationListener {
    Context ctx;
    TextView textView;
    Location loc=null;
    public MyLocationListener(Context ctx,TextView textView) {
        this.ctx=ctx;
        this.textView=textView;
    }


    @Override
    public void onLocationChanged(Location location) {
            if (location != null) {
                Toast.makeText(ctx,
                        "Location changed : Lat: " + location.getLatitude() +
                                " Lng: " + location.getLongitude(),
                        Toast.LENGTH_SHORT).show();
                textView.setText( "Location changed : Lat: " + location.getLatitude() +
                        " Lng: " + location.getLongitude());
                loc=location;
            }
        }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLoc(){
        return this.loc;
    }
}
