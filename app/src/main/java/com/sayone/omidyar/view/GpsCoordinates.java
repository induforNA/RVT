package com.sayone.omidyar.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by sayone on 16-02-2017.
 */

public class GpsCoordinates extends BaseActivity {

    private Realm realm;
    Button nextButton, backButton,saveButton, getLocButton1, getLocButton2, getLocButton3, getLocButton4, getLocButton5, getLocButton6;
    TextView landName;
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private SharedPreferences preferences;
    //    private GpsTracker gpsTracker;
    private ArrayList<Double> lngs, lats;
    //    private double curLat, curLng;
    private Context context;
    private String serveyId;
    private String currentSocialCapitalServey;
    //    private Intent gpsServiceIntent;
    GpsTrackerService gpsTrackerService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_coordinates);

        context = this;
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = preferences.getString("surveyId", "");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey", "");
        lngs = new ArrayList<>();
        lats = new ArrayList<>();

        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);
        getLocButton1 = (Button) findViewById(R.id.get_loc_button_1);
        getLocButton2 = (Button) findViewById(R.id.get_loc_button_2);
        getLocButton3 = (Button) findViewById(R.id.get_loc_button_3);
        getLocButton4 = (Button) findViewById(R.id.get_loc_button_4);
        getLocButton5 = (Button) findViewById(R.id.get_loc_button_5);
        getLocButton6 = (Button) findViewById(R.id.get_loc_button_6);
        saveButton = (Button) findViewById(R.id.button_save);
        landName = (TextView) findViewById(R.id.land_name);

        if (currentSocialCapitalServey.equals("Forestland"))
            landName.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals("Pastureland"))
            landName.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals("Mining Land"))
            landName.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals("Cropland"))
            landName.setText(getResources().getText(R.string.title_cropland));
        //  landName.setText(currentSocialCapitalServey);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        getLocButton1.setOnClickListener(this);
        getLocButton2.setOnClickListener(this);
        getLocButton3.setOnClickListener(this);
        getLocButton4.setOnClickListener(this);
        getLocButton5.setOnClickListener(this);
        getLocButton6.setOnClickListener(this);
        saveButton.setOnClickListener(this);

//        gpsServiceIntent = new Intent(this, GpsTrackerService.class);
//        startService(gpsServiceIntent);
//        bindService(gpsServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.next_button:
                Intent intent = new Intent(getApplicationContext(), SocialCapitalStartActivity.class);
                startActivity(intent);
                break;

            case R.id.back_button:
                finish();
                break;

            case R.id.save_button:

                break;

            case R.id.get_loc_button_1:
                getCoordinates(R.id.gps_loc_1, 0);
                break;

            case R.id.get_loc_button_2:
                getCoordinates(R.id.gps_loc_2, 1);
                break;

            case R.id.get_loc_button_3:
                getCoordinates(R.id.gps_loc_3, 2);
                break;

            case R.id.get_loc_button_4:
                getCoordinates(R.id.gps_loc_4, 3);
                break;

            case R.id.get_loc_button_5:
                getCoordinates(R.id.gps_loc_5, 4);
                break;

            case R.id.get_loc_button_6:
                getCoordinates(R.id.gps_loc_6, 5);
                break;
        }
    }

    public void saveCoordinates(){
        realm.beginTransaction();

    }

    public void getCoordinates(int gpsCoordinatesViewId, int index) {
//        gpsTracker = new GpsTracker(GpsCoordinates.this);
//        TextView gpsCoordinatesView = (TextView) findViewById(gpsCoordinatesViewId);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            if (gpsTracker.canGetLocation()) {
//                double latitude = gpsTracker.getLatitude();
//                double longitude = gpsTracker.getLongitude();
//                gpsTracker.stopUsingGPS();
//
//                lats.add(index, latitude);
//                lngs.add(index, longitude);
//                Log.d("Location", " GPS : " + lats.get(index) + ", " + lngs.get(index));
//                gpsCoordinatesView.setText("GPS Coordinates : " + latitude + ", " + longitude);
//            } else {
//                gpsTracker.showSettingsAlert();
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(GpsCoordinates.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale(GpsCoordinates.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                } else {
//                    ActivityCompat.requestPermissions(GpsCoordinates.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            MY_PERMISSIONS_REQUEST);
//                }
//            }
//        }
        TextView gpsCoordinatesView = (TextView) findViewById(gpsCoordinatesViewId);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mBound) {
                lats.add(index, gpsTrackerService.getLocation().getLatitude());
                lngs.add(index, gpsTrackerService.getLocation().getLongitude());
                gpsCoordinatesView.setText("GPS Coordinates : " + lats.get(index) + ", " + lngs.get(index));
                Log.d("Location", gpsTrackerService.getLocation().toString());
            }
        } else {
            if (ContextCompat.checkSelfPermission(GpsCoordinates.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(GpsCoordinates.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(GpsCoordinates.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
                }
            }
        }
    }

    protected void onStart() {
        Intent intent = new Intent(this, GpsTrackerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        super.onStart();
    }

    protected void onStop() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    if (ContextCompat.checkSelfPermission(GpsCoordinates.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(GpsCoordinates.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                        } else {
                            ActivityCompat.requestPermissions(GpsCoordinates.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST);
                        }
                    }
                }
        }


    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to GpsTrackerService, cast the IBinder and get LocalService instance
            GpsTrackerService.GpsTrackerServiceBinder binder = (GpsTrackerService.GpsTrackerServiceBinder) service;
            GpsCoordinates.this.gpsTrackerService = (GpsTrackerService) binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
