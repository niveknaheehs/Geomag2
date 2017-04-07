package com.bignerdranch.android.geomag2;


import android.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;

import java.security.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    private static final String TAG = "MainActivity";
    private final int CODE_PERMISSIONS = 1;
    private IALocationManager mIALocationManager;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;





    //private com.indooratlas.android.sdk.IALocationListener mIALocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Firebase.setAndroidContext(this);

        mFirebaseRef = new Firebase("https://geomag2-ab8c3.firebaseio.com/Paths");

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");


       String[] neededPermissions = {
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ACCESS_COARSE_LOCATION);

        }

        mIALocationManager = IALocationManager.create(this);


        //values.put("Debug", "Inside Create");//currentDateandTime);
       // mFirebaseRef.push().setValue(values);




        //Map<String,Object> values = new HashMap<>();
        //values.put("time", timeStampStr);//currentDateandTime);
        //Address location;

        //values.put("latitutde", 4);//location.getLatitude());// ;);
        //values.put("longtitue",5);// location.getLatitude()); //location.getLongitude());
        //mFirebaseRef.push().setValue(values);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Handle if any of the permissions are denied, in grantResults
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ensurePermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }
    private IALocationListener mIALocationListener = new IALocationListener() {

        // Called when the location has changed.
        @Override
        public void onLocationChanged(IALocation location) {

            String timeStampStr ;
            Map<String,Object> values = new HashMap<>();

            //Log.d(TAG, "Latitude: " + location.getLatitude());
            //Log.d(TAG, "Longitude: " + location.getLongitude());
            timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            values.put("latitutde", location.getLatitude());//location.getLatitude());// ;);
            values.put("longtitue",location.getLongitude());// location.getLatitude()); //location.getLongitude());
            mFirebaseRef.push().setValue(values);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIALocationManager.removeLocationUpdates(mIALocationListener); 
    }

}
