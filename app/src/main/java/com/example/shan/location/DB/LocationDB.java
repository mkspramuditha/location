package com.example.shan.location.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.widget.Toast;

import com.example.shan.location.LocationRecord;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chamod on 12/13/2016.
 */
public class LocationDB {
    private static LocationDB locationDB=null;
    Context context;
    DB_helper db_helper;

    public static LocationDB getInstance(Context context){
        if(locationDB==null){
            locationDB=new LocationDB(context);
        }
        return locationDB;
    }

    private LocationDB(Context context) {
        this.context=context;
        db_helper=DB_helper.getInstance(context);
    }

    public void addLocation(Location location){

        SQLiteDatabase db=db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db_helper.user_id, "1");       //correct user id must be entered except 1
        values.put(db_helper.updated_time, DateFormat.getDateTimeInstance().format(new Date()));       //DateFormat.getDateTimeInstance().format(new Date())
        values.put(db_helper.latitude,location.getLatitude());
        values.put(db_helper.longitude,location.getLongitude());
        values.put(db_helper.sent_to_server,0);

        db.insert(db_helper.locations_table, null, values);

        Toast.makeText(context, "loc added to db", Toast.LENGTH_LONG).show();
    }


    public ArrayList<LocationRecord> getAllLocationRecords(){
        ArrayList<LocationRecord> locationRecords=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s ;",db_helper.locations_table);

        Cursor cursor = db.rawQuery(query, null);

        //add account objects to a list
        while (cursor.moveToNext())
        {
            boolean sent_to_server=false;
            if(cursor.getInt(cursor.getColumnIndex(db_helper.sent_to_server))==1){
                sent_to_server=true;
            }

            LocationRecord locationRecord=new LocationRecord(cursor.getString(cursor.getColumnIndex(db_helper.user_id)),
                    cursor.getString(cursor.getColumnIndex(db_helper.updated_time)),
                    cursor.getFloat(cursor.getColumnIndex(db_helper.latitude)),
                    cursor.getFloat(cursor.getColumnIndex(db_helper.longitude)),sent_to_server);

            locationRecords.add(locationRecord);
        }

        cursor.close();

        return locationRecords;
    }
}
