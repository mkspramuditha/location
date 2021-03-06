package com.example.shan.location.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Chamod on 11/18/2016.
 */
public class DB_helper extends SQLiteOpenHelper {
    //database and table attributes
    protected static final String db_name = "location_db";
    private static DB_helper db_helper = null;
    private static final int db_version = 9;

    public static final String record_id = "record_id";

    public static final String locations_table = "locations";
    public static final String user_table = "user";

    public static final String user_password = "password";
    public static final String user_email = "email";
    public static final String user_emi_no = "emi_no";

    public static final String username = "username";
    public static final String updated_time = "updated_time";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String sent_to_server = "sent_to_server";



    public static final String time = "time";

    //singleton for DB_helper class
    public static DB_helper getInstance(Context context) {
        if (db_helper == null)
            db_helper = new DB_helper(context);
        return db_helper;
    }

    private DB_helper(Context context) {
        super(context, db_name, null, db_version);
    }

    //create account and transaction tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String locations_table_query = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s VARCHAR(30) NOT NULL ," +
                "%s VARCHAR(30),%s FLOAT,%s FLOAT,%s INT);", locations_table,record_id, user_emi_no,updated_time,latitude,longitude,sent_to_server);

        String user_table_query=String.format("CREATE TABLE %s(%s VARCHAR(20),%s VARCHAR(30),%s VARCHAR(100),%s VARCHAR(20));"
                ,user_table, username,user_emi_no,user_email,user_password);


        db.execSQL(user_table_query);
        db.execSQL(locations_table_query);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + user_table);
        db.execSQL("DROP TABLE IF EXISTS " + locations_table);
        onCreate(db);
    }

}
