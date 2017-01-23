package com.example.shan.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shan.location.DB.LocationDB;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ProgressBar progressBar1;

    private static final int REQUEST_CODE = 100;
    private ImageButton login;
//    private TextView name;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions signInOptions;


    LocationDB locationDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar1=(ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.INVISIBLE);

        locationDB = LocationDB.getInstance(this);

//        Check whether logged
        User loggedUser = locationDB.getLoggedUser();
        if (loggedUser == null) {
            Toast.makeText(this, "Please Login...!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, LoggedActivity.class);
//        pass username and password variables
            intent.putExtra("var_username", loggedUser.getUsername());//correct to the response username
            intent.putExtra("var_password", loggedUser.getPassword());//correct to the response username

            startActivity(intent);
        }


        //for google sign in

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        login = (ImageButton) findViewById(R.id.regbtn);
//        name = (TextView) findViewById(R.id.name);

//        login.setScrollBarSize(SignInButton.SIZE_WIDE);
//        login.setScopes(signInOptions.getScopeArray());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQUEST_CODE);

                progressBar1.setVisibility(View.VISIBLE);
            }
        });


    }


    public void getServerAuthentication(final String email) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Connecting to server...");
        pDialog.show();


//               test emi
        TelephonyManager tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String emi_no=tm.getDeviceId();
//
        final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("imie",emi_no);
            jsonObject.put("appId","1234");                         //set correct app id and service provider
            jsonObject.put("serviceProvider","Dialog");
            jsonObject.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        String URL = "http://128.199.173.183/routeradar/web/app_dev.php/client-app-all/registration";

        final String mRequestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                try {
                    JSONObject j=new JSONObject(response.split("abc\"")[1]);

                    Toast.makeText(LoginActivity.this,j.toString() ,Toast.LENGTH_LONG).show();
                    logUser(email,emi_no,j.getString("username"),j.getString("password"));

                    pDialog.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("VOLLEY", error.getLocalizedMessage());
                Toast.makeText(LoginActivity.this,"Login Failed...!",Toast.LENGTH_LONG).show();
                pDialog.hide();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody);
                    return null;
                }

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String creds = String.format("%s:%s", "xxxxxx", "xxxxxxxxxxxxxx");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


// Adding request to request queue
        requestQueue.add(stringRequest);
    }


    private void logUser(String email,String emi_no,String username,String password){
//        Add to databaase
        locationDB.setUser(username,emi_no,email, password);


        Intent intent = new Intent(this, LoggedActivity.class);
//        pass username and password variables
        intent.putExtra("var_username", username);//correct to the response username
        intent.putExtra("var_password", password);//correct to the response username

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this,resultCode+"",Toast.LENGTH_LONG).show();

        if (requestCode == REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println(result);
            System.out.println(result.isSuccess());

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
//                idToken = acct.getIdToken();
//                personEmail = acct.getEmail();
                System.out.println("Your Email is: " + acct.getEmail());
                Toast.makeText(getApplicationContext(), acct.getEmail() + "", Toast.LENGTH_LONG).show();
                //after google sign in success read permissions
//                checkReadPhoneStatePermission();

//                Connect to server to geet username password
                getServerAuthentication(acct.getEmail());

            } else {
                Toast.makeText(getApplicationContext(), "Registartion Failed...!", Toast.LENGTH_LONG).show();
            }

        }

        progressBar1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}