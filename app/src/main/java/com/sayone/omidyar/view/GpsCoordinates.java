package com.sayone.omidyar.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Component;
import com.sayone.omidyar.model.ParcelLocation;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sayone on 16-02-2017.
 */

public class GpsCoordinates extends BaseActivity {

    private Realm realm;
    Button nextButton, backButton, saveButton, getLocButton1, getLocButton2, getLocButton3, getLocButton4, getLocButton5, getLocButton6;
    TextView landName;
    TextView gpsLocation_1, gpsLocation_2, gpsLocation_3, gpsLocation_4, gpsLocation_5, gpsLocation_6;
    EditText parcelArea;
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private SharedPreferences preferences;
    private Location[] corners;
    private float area;
    private Context context;
    private String surveyId;
    private String currentSocialCapitalServey;
    GpsTrackerService gpsTrackerService;
    boolean mBound = false;
    boolean isSaved = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_coordinates);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = preferences.getString("surveyId", "");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey", "");

        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);
        getLocButton1 = (Button) findViewById(R.id.get_loc_button_1);
        getLocButton2 = (Button) findViewById(R.id.get_loc_button_2);
        getLocButton3 = (Button) findViewById(R.id.get_loc_button_3);
        getLocButton4 = (Button) findViewById(R.id.get_loc_button_4);
        getLocButton5 = (Button) findViewById(R.id.get_loc_button_5);
        getLocButton6 = (Button) findViewById(R.id.get_loc_button_6);
        gpsLocation_1 = (TextView) findViewById(R.id.gps_loc_1);
        gpsLocation_2 = (TextView) findViewById(R.id.gps_loc_2);
        gpsLocation_3 = (TextView) findViewById(R.id.gps_loc_3);
        gpsLocation_4 = (TextView) findViewById(R.id.gps_loc_4);
        gpsLocation_5 = (TextView) findViewById(R.id.gps_loc_5);
        gpsLocation_6 = (TextView) findViewById(R.id.gps_loc_6);
        saveButton = (Button) findViewById(R.id.save_button);
        landName = (TextView) findViewById(R.id.land_name);
        parcelArea = (EditText) findViewById(R.id.parcel_area_edit);
        corners = new Location[6];

        if (currentSocialCapitalServey.equals("Forestland"))
            landName.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals("Pastureland"))
            landName.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals("Mining Land"))
            landName.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals("Cropland"))
            landName.setText(getResources().getText(R.string.title_cropland));
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        getLocButton1.setOnClickListener(this);
        getLocButton2.setOnClickListener(this);
        getLocButton3.setOnClickListener(this);
        getLocButton4.setOnClickListener(this);
        getLocButton5.setOnClickListener(this);
        getLocButton6.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        getLocButton1.requestFocus();

        loadSavedCoordinates();
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
                saveInputs();
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

    private void loadSavedCoordinates() {
        RealmResults<ParcelLocation> parcelLocations = realm.where(ParcelLocation.class).
                equalTo("surveyId", surveyId)
                .findAll();

        if (parcelLocations.size() >= 0) {
            for (ParcelLocation parcelLocation : parcelLocations) {
                Location temp = new Location("");

                temp.setLatitude(parcelLocation.getLat_1());
                temp.setLongitude(parcelLocation.getLng_1());
                corners[0] = temp;
                temp.setLatitude(parcelLocation.getLat_2());
                temp.setLongitude(parcelLocation.getLng_2());
                corners[1] = temp;
                temp.setLatitude(parcelLocation.getLat_3());
                temp.setLongitude(parcelLocation.getLng_3());
                corners[2] = temp;
                temp.setLatitude(parcelLocation.getLat_4());
                temp.setLongitude(parcelLocation.getLng_4());
                corners[3] = temp;
                temp.setLatitude(parcelLocation.getLat_5());
                temp.setLongitude(parcelLocation.getLng_5());
                corners[4] = temp;
                temp.setLatitude(parcelLocation.getLat_6());
                temp.setLongitude(parcelLocation.getLng_6());
                corners[5] = temp;

                String coordinate_1 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_1()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_1());
                String coordinate_2 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_2()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_2());
                String coordinate_3 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_3()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_3());
                String coordinate_4 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_4()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_4());
                String coordinate_5 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_5()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_5());
                String coordinate_6 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_6()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_6());
                String parcelSize = Float.toString(parcelLocation.getArea());

                gpsLocation_1.setText("Coordinates : " + coordinate_1);
                gpsLocation_2.setText("Coordinates : " + coordinate_2);
                gpsLocation_3.setText("Coordinates : " + coordinate_3);
                gpsLocation_4.setText("Coordinates : " + coordinate_4);
                gpsLocation_5.setText("Coordinates : " + coordinate_5);
                gpsLocation_6.setText("Coordinates : " + coordinate_6);
                parcelArea.setText(parcelSize);
                area = parcelLocation.getArea();
            }
        }
    }

    private void saveInputs() {

        if (validateInputs()) {
            Toast.makeText(GpsCoordinates.this, "Saving", Toast.LENGTH_SHORT).show();
            surveyId = preferences.getString("surveyId", "");

            Survey survey = realm.where(Survey.class).
                    equalTo("surveyId", surveyId)
                    .findFirst();

            for (int i = 0; i < 6; i++) {
                Log.d("GPS", "Location: " + corners[i]);
            }

            realm.beginTransaction();
            ParcelLocation parcelLocation = realm.createObject(ParcelLocation.class);
            parcelLocation.setId(getNextKeyComponent());
            parcelLocation.setSurveyId(surveyId);
            parcelLocation.setLat_1(corners[0].getLatitude());
            parcelLocation.setLat_2(corners[1].getLatitude());
            parcelLocation.setLat_3(corners[2].getLatitude());
            parcelLocation.setLat_4(corners[3].getLatitude());
            parcelLocation.setLat_5(corners[4].getLatitude());
            parcelLocation.setLat_6(corners[5].getLatitude());
            parcelLocation.setLng_1(corners[0].getLongitude());
            parcelLocation.setLng_2(corners[1].getLongitude());
            parcelLocation.setLng_3(corners[2].getLongitude());
            parcelLocation.setLng_4(corners[3].getLongitude());
            parcelLocation.setLng_5(corners[4].getLongitude());
            parcelLocation.setLng_6(corners[5].getLongitude());
            parcelLocation.setArea(area);
            realm.copyToRealmOrUpdate(parcelLocation);
            realm.commitTransaction();

            realm.beginTransaction();
            survey.setParcelLocations(parcelLocation);
            realm.commitTransaction();

            Toast.makeText(GpsCoordinates.this, "Saved", Toast.LENGTH_SHORT).show();
            isSaved = true;
        } else {
            Toast.makeText(GpsCoordinates.this, "Please get all corners and enter the area", Toast.LENGTH_SHORT).show();
            isSaved = false;
        }

    }

    public int getNextKeyComponent() {
        return realm.where(ParcelLocation.class).max("id").intValue() + 1;
    }

    private boolean validateInputs() {
        String areaString = parcelArea.getText().toString();

        for (int i = 0; i < 6; i++) {
            if (corners[i] == null || corners[i].getLatitude() == 0 || corners[i].getLongitude() == 0) {
                return false;
            }
        }

        if (areaString.equals("")) {
            return false;
        }

        area = Float.parseFloat(areaString);

        return true;
    }

    public void getCoordinates(int gpsCoordinatesViewId, int index) {

        TextView gpsCoordinatesView = (TextView) findViewById(gpsCoordinatesViewId);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mBound) {
                Location location = gpsTrackerService.getLocation();
                if (location == null) {
                    gpsCoordinatesView.setText("GPS Coordinates : -");
                } else if (location.getLatitude() == 0 || location.getLongitude() == 0) {
                    gpsCoordinatesView.setText("GPS Coordinates : -");
                } else {
                    corners[index] = new Location("");
                    corners[index].setLatitude(location.getLatitude());
                    corners[index].setLongitude(location.getLongitude());
                    String latDMS = LocationConverter.getLatitudeDMS(corners[index].getLatitude());
                    String lngDMS = LocationConverter.getLongitudeDMS(corners[index].getLongitude());
                    int accuracy = (int) location.getAccuracy();
                    gpsCoordinatesView.setText("GPS Coordinates : " + latDMS + ", " + lngDMS + " (Accuracy : " + accuracy + "m)");
                    Log.d("GPS", "Coordinates: " + corners[index].getLatitude() + ", " + corners[index].getLongitude() + "(Accuracy : " + accuracy + "m)");
                }
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
        if (ContextCompat.checkSelfPermission(GpsCoordinates.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GpsCoordinates.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(GpsCoordinates.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
        }
        startLocationService();
        super.onStart();
    }

    protected void onStop() {
        stopLocationService();
        super.onStop();
    }

    private void startLocationService() {
        intent = new Intent(this, GpsTrackerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopLocationService() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        stopService(intent);
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
