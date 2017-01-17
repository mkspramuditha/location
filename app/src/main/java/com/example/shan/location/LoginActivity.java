package com.example.shan.location;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void RegButtonClicked(View v) {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        Posting server url
        String url = "https://httpbin.org/post";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Connecting to server...");
        pDialog.show();

//Posting parameters
        Map<String, String> params = new HashMap<>();
        params.put("custname", "Chamod");
        params.put("custtel", "07122222");
        params.put("custemail", "eeeee");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Handle the response json object
                        try {
                            Toast.makeText(LoginActivity.this,response.getString("data"),Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException e){}
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }) {

        };

// Adding request to request queue
        requestQueue.add(jsonObjReq);

    }

}
