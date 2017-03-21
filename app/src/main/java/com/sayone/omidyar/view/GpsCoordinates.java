package com.sayone.omidyar.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.ParcelLocation;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;

/**
 * Created by sayone on 16-02-2017.
 */

public class GpsCoordinates extends BaseActivity {

    private Realm realm;
    Button nextButton, backButton, getLocButton1, getLocButton2, getLocButton3, getLocButton4, getLocButton5, getLocButton6;
    TextView gpsLocation_1, gpsLocation_2, gpsLocation_3, gpsLocation_4, gpsLocation_5, gpsLocation_6;
    public static TextView gpsStatusText;
    public static ProgressBar gpsSearchingIndicator;
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
    private String parcelSize;
    LayoutInflater inflater;

    //Side Nav
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;
    private TextView textViewAbout;
    private TextView startSurvey;
    private TextView harvestingForestProducts;
    private TextView agriculture;
    private TextView grazing;
    private TextView mining;
    private TextView sharedCostsOutlays;
    private TextView certificate;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_coordinates);
        setContentView(R.layout.activity_gps_coordinates);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = preferences.getString("surveyId", "");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalSurvey", "");

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
        gpsStatusText = (TextView) findViewById(R.id.gps_status_text);
        gpsSearchingIndicator = (ProgressBar) findViewById(R.id.gps_searching_indicator);
        corners = new Location[6];
        for (int i = 0; i < 6; i++) {
            corners[i] = new Location("");
        }

        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        getLocButton1.setOnClickListener(this);
        getLocButton2.setOnClickListener(this);
        getLocButton3.setOnClickListener(this);
        getLocButton4.setOnClickListener(this);
        getLocButton5.setOnClickListener(this);
        getLocButton6.setOnClickListener(this);
        getLocButton1.requestFocus();
        parcelSize = "";
        inflater = this.getLayoutInflater();
        loadSavedCoordinates();

        //Side Nav
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
        harvestingForestProducts = (TextView) findViewById(R.id.text_harvesting_forest_products);
        agriculture = (TextView) findViewById(R.id.text_agriculture);
        grazing = (TextView) findViewById(R.id.text_grazing);
        mining = (TextView) findViewById(R.id.text_mining);
        sharedCostsOutlays = (TextView) findViewById(R.id.text_shared_costs_outlays);
        certificate = (TextView) findViewById(R.id.text_certificate);
        logout = (TextView) findViewById(R.id.logout);
        textViewAbout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        harvestingForestProducts.setOnClickListener(this);
        agriculture.setOnClickListener(this);
        grazing.setOnClickListener(this);
        mining.setOnClickListener(this);
        sharedCostsOutlays.setOnClickListener(this);
        certificate.setOnClickListener(this);
        logout.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);
        setNav();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.next_button:
                getParcelArea();
                break;

            case R.id.back_button:
                finish();
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

            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;

            case R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;

            case R.id.text_view_about:
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                break;

            case R.id.text_start_survey:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent intents = new Intent(getApplicationContext(), RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;
            case R.id.text_harvesting_forest_products:
                setCurrentSocialCapitalSurvey(getString(R.string.string_forestland));
                startLandTypeActivity();
                break;
            case R.id.text_agriculture:
                setCurrentSocialCapitalSurvey(getString(R.string.string_cropland));
                startLandTypeActivity();
                break;
            case R.id.text_grazing:
                setCurrentSocialCapitalSurvey(getString(R.string.string_pastureland));
                startLandTypeActivity();
                break;
            case R.id.text_mining:
                setCurrentSocialCapitalSurvey(getString(R.string.string_miningland));
                startLandTypeActivity();
                break;
            case R.id.text_shared_costs_outlays:
                Intent intent_outlay = new Intent(getApplicationContext(), SharedCostSurveyStartActivity.class);
                startActivity(intent_outlay);
                break;
            case R.id.text_certificate:
                Intent intent_certificate = new Intent(getApplicationContext(), NewCertificateActivity.class);
                startActivity(intent_certificate);
                break;
        }
    }

    private void loadSavedCoordinates() {
        Survey survey = realm.where(Survey.class).
                equalTo("surveyId", surveyId)
                .findFirst();
        ParcelLocation parcelLocation;

        if (survey.getParcelLocations() != null) {
            parcelLocation = survey.getParcelLocations();
            corners[0].setLatitude(parcelLocation.getLat_1());
            corners[0].setLongitude(parcelLocation.getLng_1());
            corners[1].setLatitude(parcelLocation.getLat_2());
            corners[1].setLongitude(parcelLocation.getLng_2());
            corners[2].setLatitude(parcelLocation.getLat_3());
            corners[2].setLongitude(parcelLocation.getLng_3());
            corners[3].setLatitude(parcelLocation.getLat_4());
            corners[3].setLongitude(parcelLocation.getLng_4());
            corners[4].setLatitude(parcelLocation.getLat_5());
            corners[4].setLongitude(parcelLocation.getLng_5());
            corners[5].setLatitude(parcelLocation.getLat_6());
            corners[5].setLongitude(parcelLocation.getLng_6());

            String coordinate_1 = parcelLocation.getCoordinateOne();
            String coordinate_2 = parcelLocation.getCoordinateTwo();
            String coordinate_3 = parcelLocation.getCoordinateThree();
            String coordinate_4 = parcelLocation.getCoordinateFour();
            String coordinate_5 = parcelLocation.getCoordinateFive();
            String coordinate_6 = parcelLocation.getCoordinateSix();

            parcelSize = Float.toString(parcelLocation.getArea());

            gpsLocation_1.setText("GPS Coordinates : " + coordinate_1);
            gpsLocation_2.setText("GPS Coordinates : " + coordinate_2);
            gpsLocation_3.setText("GPS Coordinates : " + coordinate_3);
            gpsLocation_4.setText("GPS Coordinates : " + coordinate_4);
            gpsLocation_5.setText("GPS Coordinates : " + coordinate_5);
            gpsLocation_6.setText("GPS Coordinates : " + coordinate_6);
            area = parcelLocation.getArea();
        }
    }

    private void getParcelArea() {

        Survey survey = realm.where(Survey.class).
                equalTo("surveyId", surveyId)
                .findFirst();
        ParcelLocation parcelLocation;

        if (survey.getParcelLocations() != null) {
            parcelLocation = survey.getParcelLocations();
            parcelSize = Float.toString(parcelLocation.getArea());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.parcel_area, null);
//        builder.setTitle("Parcel Area");
        final EditText areaInput = (EditText) dialogView.findViewById(R.id.parcel_area_edit);
        final RadioGroup parcelAreaAns = (RadioGroup) dialogView.findViewById(R.id.parcel_area_ques_ans);
        final LinearLayout parcelAreaInputLayout = (LinearLayout) dialogView.findViewById(R.id.parcel_area_input_layout);

        parcelAreaAns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.parcel_area_ques_yes) {
                    parcelAreaInputLayout.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.parcel_area_ques_no) {
                    parcelAreaInputLayout.setVisibility(View.GONE);
                }
            }
        });

        if (!parcelSize.equals("") && !parcelSize.equals("0.0")) {
            areaInput.setText(parcelSize);
        }
        builder.setView(dialogView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (parcelAreaInputLayout.getVisibility() == View.VISIBLE) {
                    String areaString = areaInput.getText().toString();
                    area = (areaString.equals("")) ? -1 : Float.parseFloat(areaString);
                } else {
                    area = 0;
                }
                saveInputs();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.setCancelable(false);
//        builder.show();

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(200, 100); //Controlling width and height.
        alertDialog.show();
    }

    private void saveInputs() {

        if (validateInputs()) {
            surveyId = preferences.getString("surveyId", "");

            Survey survey = realm.where(Survey.class).
                    equalTo("surveyId", surveyId)
                    .findFirst();

            realm.beginTransaction();
            ParcelLocation parcelLocation = realm.createObject(ParcelLocation.class);
            parcelLocation.setId(getNextKeyComponent());
            parcelLocation.setSurveyId(surveyId);
            parcelLocation.setLat_1((corners[0] == null) ? 0 : corners[0].getLatitude());
            parcelLocation.setLat_2((corners[1] == null) ? 0 : corners[1].getLatitude());
            parcelLocation.setLat_3((corners[2] == null) ? 0 : corners[2].getLatitude());
            parcelLocation.setLat_4((corners[3] == null) ? 0 : corners[3].getLatitude());
            parcelLocation.setLat_5((corners[4] == null) ? 0 : corners[4].getLatitude());
            parcelLocation.setLat_6((corners[5] == null) ? 0 : corners[5].getLatitude());
            parcelLocation.setLng_1((corners[0] == null) ? 0 : corners[0].getLongitude());
            parcelLocation.setLng_2((corners[1] == null) ? 0 : corners[1].getLongitude());
            parcelLocation.setLng_3((corners[2] == null) ? 0 : corners[2].getLongitude());
            parcelLocation.setLng_4((corners[3] == null) ? 0 : corners[3].getLongitude());
            parcelLocation.setLng_5((corners[4] == null) ? 0 : corners[4].getLongitude());
            parcelLocation.setLng_6((corners[5] == null) ? 0 : corners[5].getLongitude());
            parcelLocation.setArea(area);
            realm.copyToRealmOrUpdate(parcelLocation);
            realm.commitTransaction();

            realm.beginTransaction();
            survey.setParcelLocations(parcelLocation);
            realm.commitTransaction();

            Toast.makeText(GpsCoordinates.this, "Saved", Toast.LENGTH_SHORT).show();
            isSaved = true;
            Intent intent = new Intent(getApplicationContext(), StartLandTypeActivity.class);
            startActivity(intent);
        } else {
            isSaved = false;
        }

    }

    public int getNextKeyComponent() {
        return realm.where(ParcelLocation.class).max("id").intValue() + 1;
    }

    private boolean validateInputs() {
//        int nullCount = 0;
//        for (int i = 0; i < 6; i++) {
//            if (corners[i] == null || corners[i].getLatitude() == 0 || corners[i].getLongitude() == 0) {
//                nullCount++;
//            }
//        }
//
//        if (nullCount > 3) {
//            Toast.makeText(GpsCoordinates.this, "Please set at least three corners", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (Float.isNaN(area) || area == -1) {
            Toast.makeText(GpsCoordinates.this, "Please enter a valid parcel area", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void getCoordinates(int gpsCoordinatesViewId, int index) {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showNoGpsAlert();
        } else {
            TextView gpsCoordinatesView = (TextView) findViewById(gpsCoordinatesViewId);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mBound) {
                    Location location = gpsTrackerService.getLocation();
                    if (location == null) {
                        gpsCoordinatesView.setText("GPS Coordinates : Not set");
                    } else if (location.getLatitude() == 0 || location.getLongitude() == 0) {
                        gpsCoordinatesView.setText("GPS Coordinates : Not set");
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
    }

    private void showNoGpsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
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

    private void setNav() {
        harvestingForestProducts.setVisibility(View.GONE);
        agriculture.setVisibility(View.GONE);
        grazing.setVisibility(View.GONE);
        mining.setVisibility(View.GONE);

        if (checkLandKind(getString(R.string.string_forestland))) {
            harvestingForestProducts.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_cropland))) {
            agriculture.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_pastureland))) {
            grazing.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_miningland))) {
            mining.setVisibility(View.VISIBLE);
        }
    }

    private void startLandTypeActivity() {
        Intent intent = new Intent(GpsCoordinates.this, StartLandTypeActivity.class);
        startActivity(intent);
    }

    private void setCurrentSocialCapitalSurvey(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("currentSocialCapitalSurvey", name);
        editor.apply();
    }

    private boolean checkLandKind(String name) {
        LandKind landKind = realm.where(LandKind.class)
                .equalTo("name", name)
                .equalTo("surveyId", surveyId)
                .findFirst();
        if (landKind.getStatus().equals("active")) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
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
