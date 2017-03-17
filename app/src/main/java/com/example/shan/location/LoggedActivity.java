package com.example.shan.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class LoggedActivity extends AppCompatActivity {


    public static final long REPEAT_TIME=5*60*1000;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);



        String var_username=getIntent().getStringExtra("var_username");
        String var_password=getIntent().getStringExtra("var_password");


        ((TextView)findViewById(R.id.username)).setText(var_username);
        ((TextView)findViewById(R.id.password)).setText(var_password);

//        Start Services
        if(checkLocationPermissions())
        {
            startLocationService();
        }

    }

    @Override
    public void onBackPressed() {
//        Cleared back button action
//        super.onBackPressed();
    }


    public void startLocationService(){
        setLocationAlarm();
        Toast.makeText(this,"Tracking started...",Toast.LENGTH_SHORT).show();
    }


    private void setLocationAlarm(){
//        Intent intent = new Intent(this, LocationService.class);
        Intent intent = new Intent(this, AlarmReceiver.class);
        Calendar cl = Calendar.getInstance();
//        PendingIntent location_pending_intent = PendingIntent.getService(this, 0, intent, 0);
        PendingIntent location_pending_intent = PendingIntent.getBroadcast(this.getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 5 minute
//        alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), location_pending_intent);
    alarm.setRepeating(AlarmManager.RTC_WAKEUP,cl.getTimeInMillis(),REPEAT_TIME,location_pending_intent);
    }



    private boolean checkLocationPermissions(){
        if(ActivityCompat.checkSelfPermission(LoggedActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestFineLocationPermission();
            return false;
        }


        if(ActivityCompat.checkSelfPermission(LoggedActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestCoarseLocationPermission();
            return false;
        }
        return true;
    }

    /**
     * Requests the ACCESS_FINE_LOCATION permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestFineLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(LoggedActivity.this)
                    .setTitle("Warning!")
                    .setMessage("Do you want to track this device, Please allow the permission request")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(LoggedActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Requests the ACCESS_COARSE_LOCATION permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestCoarseLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(LoggedActivity.this)
                    .setTitle("Warning!")
                    .setMessage("Do you want to track this device, Please allow the permission request")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(LoggedActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    startLocationService();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(LoggedActivity.this,"Please provide location permissions to track the device...!",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
