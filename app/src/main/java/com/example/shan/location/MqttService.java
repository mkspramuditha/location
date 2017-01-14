package com.example.shan.location;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by shan on 1/14/17.
 */

public class MqttService extends IntentService {

    public MqttService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
