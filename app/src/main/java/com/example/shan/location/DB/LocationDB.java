package com.example.shan.location.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.shan.location.LocationRecord;
import com.example.shan.location.User;

import java.util.ArrayList;

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

//    return logged user iin the phone
    public User getLoggedUser(){

//        added only for testing

//        return new User("Shan","1234555600","pass");

        SQLiteDatabase db=db_helper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s ;",DB_helper.user_table);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext())
        {
            return new User(cursor.getString(cursor.getColumnIndex(DB_helper.username)),
                    cursor.getString(cursor.getColumnIndex(DB_helper.user_emi_no)),
                    cursor.getString(cursor.getColumnIndex(DB_helper.user_password)));
        }

        cursor.close();

        return null;

    }

    public void setUser(String username,String emi,String email,String password){
        SQLiteDatabase db=db_helper.getWritableDatabase();

//        Delete other users
        db.execSQL("delete from "+DB_helper.user_table);

//        insert new user
        ContentValues cv=new ContentValues();
        cv.put(DB_helper.username,username);
        cv.put(DB_helper.user_emi_no,emi);
        cv.put(DB_helper.user_email,email);
        cv.put(DB_helper.user_password,password);
        db.insert(DB_helper.user_table, null, cv);

    }

    public void addLocation(Location location,String current_time){

        SQLiteDatabase db=db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_helper.user_emi_no, getLoggedUser().getEmi());
        values.put(DB_helper.updated_time, current_time);       //DateFormat.getDateTimeInstance().format(new Date())
        values.put(DB_helper.latitude,location.getLatitude());
        values.put(DB_helper.longitude,location.getLongitude());
        values.put(DB_helper.sent_to_server,0);
        db.insert(DB_helper.locations_table, null, values);

        Toast.makeText(context, "Location added to the database", Toast.LENGTH_LONG).show();
    }


    public ArrayList<LocationRecord> getPendingLocationRecords(){
        ArrayList<LocationRecord> locationRecords=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s=0;",DB_helper.locations_table,DB_helper.sent_to_server);

        Cursor cursor = db.rawQuery(query, null);

        //add account objects to a list
        while (cursor.moveToNext())
        {
            LocationRecord locationRecord=new LocationRecord(
                    cursor.getInt(cursor.getColumnIndex(DB_helper.record_id)),
                    cursor.getString(cursor.getColumnIndex(DB_helper.user_emi_no)),
                    cursor.getString(cursor.getColumnIndex(DB_helper.updated_time)),
                    cursor.getFloat(cursor.getColumnIndex(DB_helper.latitude)),
                    cursor.getFloat(cursor.getColumnIndex(DB_helper.longitude)),
                    false);

            locationRecords.add(locationRecord);
        }

        cursor.close();

        return locationRecords;
    }

    public void sentToServer(int record_id){
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DB_helper.sent_to_server,1);
        db.update(DB_helper.locations_table,cv,DB_helper.record_id+"="+record_id,null);
    }
}
