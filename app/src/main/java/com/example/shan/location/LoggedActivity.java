package com.example.shan.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoggedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);


        String var_username=getIntent().getStringExtra("var_username");
        String var_password=getIntent().getStringExtra("var_password");

        ((TextView)findViewById(R.id.username)).setText(var_username);
        ((TextView)findViewById(R.id.password)).setText(var_password);
    }

    @Override
    public void onBackPressed() {
//        Cleared back button action
//        super.onBackPressed();
    }
}
