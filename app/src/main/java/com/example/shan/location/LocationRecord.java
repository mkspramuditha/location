package com.example.shan.location;

/**
 * Created by Chamod on 12/13/2016.
 */
public class LocationRecord {
    private int record_id;
    private float latitude;
    private float longitude;
    private String updated_time;
    private String emi_no;
    private boolean sent_to_server;

    public LocationRecord(int record_id,String emi_no,String updated_time,float latitude, float longitude,boolean sent_to_server) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.updated_time = updated_time;
        this.emi_no = emi_no;
        this.sent_to_server=sent_to_server;
        this.record_id=record_id;
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

    public String getEmi_no() {
        return emi_no;
    }

    public boolean isSent_to_server() {
        return sent_to_server;
    }
}
