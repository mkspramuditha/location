package com.example.shan.location.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.shan.location.LocationRecord;
import com.example.shan.location.User;

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

    boolean stopped=false;

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

//    return logged user iin the phone
    public User getLoggedUser(){
        SQLiteDatabase db=db_helper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s ;",db_helper.user_table);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext())
        {
            return new User(cursor.getString(cursor.getColumnIndex(db_helper.user_id)),
                    cursor.getString(cursor.getColumnIndex(db_helper.user_password)));
        }

        cursor.close();

        return null;

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

        //Toast.makeText(context, "loc added to db", Toast.LENGTH_LONG).show();
    }


    public ArrayList<LocationRecord> getPendingLocationRecords(){
        ArrayList<LocationRecord> locationRecords=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s=0;",db_helper.locations_table,DB_helper.sent_to_server);

        Cursor cursor = db.rawQuery(query, null);

        //add account objects to a list
        while (cursor.moveToNext())
        {
            LocationRecord locationRecord=new LocationRecord(cursor.getString(cursor.getColumnIndex(db_helper.user_id)),
                    cursor.getString(cursor.getColumnIndex(db_helper.updated_time)),
                    cursor.getFloat(cursor.getColumnIndex(db_helper.latitude)),
                    cursor.getFloat(cursor.getColumnIndex(db_helper.longitude)),false);

            locationRecords.add(locationRecord);
        }

        cursor.close();

        return locationRecords;
    }
}
