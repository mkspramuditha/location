package com.example.shan.location;

/**
 * Created by Chamod on 12/13/2016.
 */
public class LocationRecord {
    private int record_id;
    private float latitude;
    private float longitude;
    private String updated_time;
    private String user_id;
    private boolean sent_to_server;

    public LocationRecord(String user_id,String updated_time,float latitude, float longitude,boolean sent_to_server) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.updated_time = updated_time;
        this.user_id = user_id;
        this.sent_to_server=sent_to_server;
    }

    public int getRecord_id() {
        return record_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public boolean isSent_to_server() {
        return sent_to_server;
    }
}
