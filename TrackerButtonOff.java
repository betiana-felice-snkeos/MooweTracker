package tracker.moowe.de.moowe;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import android.content.SharedPreferences;


import static android.provider.Telephony.Carriers.PASSWORD;

public class TrackerButtonOff extends AppCompatActivity {
    private Button start;
    private Button stopButton;
    private Button walk;
    private Button bike;
    private Button car;
    private Button multi;
    private Button bus;
    private Button tram;
    private Button ubahn;
    private Button sbahn;
    private String uniqueID;
    private String URL;
    private String requestBody;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private JSONObject jsonBody;
    private JSONArray   jsonArray;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ArrayList<Geolocation> geolocation = new ArrayList<Geolocation>();
    private String mot;
    private boolean sentToSettings = false;
    private SharedPreferences on;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_button_off);

        String uniqueID = UUID.randomUUID().toString();
        String URL = " http://ec2-54-201-218-107.us-west-2.compute.amazonaws.com:5000/api/add_location/uniqueID" ;
        requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        start = (Button) findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stopButton);
        walk = (Button) findViewById(R.id.walk);
        bike = (Button) findViewById(R.id.bike);
        car = (Button) findViewById(R.id.car);
        multi = (Button) findViewById(R.id.multi);
        bus = (Button) findViewById(R.id.bus);
        tram = (Button) findViewById(R.id.tram);
        ubahn = (Button) findViewById(R.id.ubahn);
        sbahn = (Button) findViewById(R.id.sbahn);

        final ArrayList<Geolocation> geolocation = new ArrayList<Geolocation>();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geolocation.add(new Geolocation(location.getLatitude(),location.getLongitude(),location.getTime(),mot));
                  }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            },10);
            return;
        }else{
            configureButton();
        }
        try {
            jsonBody.put("Geolocation", geolocation);
            final String requestBody = jsonBody.toString();


        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        VolleyLog.v("Response: ", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.toString());

                    }
                }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //add params <key,value>
                params.put("Test", "Test me!");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                // add headers <key,value>
                String credentials = "android:betiana";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

               requestQueue.add(postRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
        }
    }

    private void configureButton(){
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                requestQueue.start();
                locationManager.removeUpdates(locationListener);
            }

        });
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot = "walk";
            }
        });
        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot = "bike";
            }
        });
        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mot="multi";
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot = "bus";
            }
        });
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot="car";
            }
        });
        ubahn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot = "ubahn";
            }
        });
        sbahn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot = "sbahn";
            }
        });
        tram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mot = "tram";
            }
        });

        }

    }
