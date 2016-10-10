package com.sayone.omidyar.view;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.Omidyar;
import com.sayone.omidyar.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class OmidyarMap extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.SnapshotReadyCallback{



    Button nextButton, backButton,drawPolygon,clear,submit;
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private GoogleMap mMap;
    double lat = 0, lon = 0,initLat,initLon;
    GoogleApiClient mGoogleApiClient = null;
    ImageView mapImage;
    private Location mLastLocation = null;
    boolean flag = true,initFlag=true;
    Bitmap bitmap;


    ArrayList<Double> x,y;
    private LatLng latLng1;
    private LatLng point;
    private PolylineOptions polylineOptions;
    private int temp;
    private View map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omidyar_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        x=new ArrayList<>();
        y=new ArrayList<>();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        polylineOptions = new PolylineOptions();

        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);
        drawPolygon = (Button)findViewById(R.id.draw_map_button);
        clear = (Button)findViewById(R.id.clear_button);
        submit = (Button)findViewById(R.id.submit_button);
        mapImage = (ImageView)findViewById(R.id.map_image);
        map = (View)findViewById(R.id.map);


        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        drawPolygon.setOnClickListener(this);
        clear.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        Log.e("lat ", "" + lat);
        Log.e("lon ", "" + lon);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    if(flag){
                        googleMap.clear();
                        flag=false;
                    }
                    latLng1=latLng;
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);
                    if(initFlag){
                        initLat=latLng.latitude;
                        initLon=latLng.longitude;
                        initFlag=false;
                    }
                    x.add(latLng.latitude);
                    y.add(latLng.longitude);


                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                    // Clears the previously touched position
                   // googleMap.clear();

                    // Animating to the touched position
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Placing a marker on the touched position
                    googleMap.addMarker(markerOptions);



                    // polylineOptions = new PolylineOptions();




                }
            });








    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.next_button:
                Intent intent=new Intent(getApplicationContext(),SocialCapitalStartActivity.class);
                startActivity(intent);
                break;

            case R.id.back_button:
                finish();
                break;

            case R.id.submit_button:
                View v = map.getRootView();
                v.setDrawingCacheEnabled(true);
                mMap.snapshot(this);
                mapImage.setImageBitmap(bitmap);



                break;

            case R.id.clear_button:

                x.clear();
                y.clear();
                initLon=0;
                initFlag=true;
                initLat=0;
                mMap.clear();
                polylineOptions = new PolylineOptions();
                break;

            case R.id.draw_map_button:
                mMap.clear();
                int i = 0;
                for(Double cordinate:x){
                    Log.e("LAT LONG ", cordinate+" "+y.get(i));

                    polylineOptions.add(new LatLng(cordinate,y.get(i)));
                    i++;
                    temp=i;
                    mMap.addPolyline(polylineOptions).setWidth(3);
                }
                // polylineOptions.add(new LatLng(x.get(temp),y.get(temp)));
                polylineOptions.add(new LatLng(initLat,initLon));
                mMap.addPolyline(polylineOptions).setWidth(3);
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mLastLocation.getLatitude();
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();

                Log.e("LAT ",lat+"");
                Log.e("LON ",lon+"");
                LatLng sydney = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(sydney).title("your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                return;

            }

        }
        else{

            if (ContextCompat.checkSelfPermission(OmidyarMap.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(OmidyarMap.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {
                    ActivityCompat.requestPermissions(OmidyarMap.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST);
                }
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("suspended", "suspended");
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("failed", "failed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    return;
                }
                else{
                    if (ContextCompat.checkSelfPermission(OmidyarMap.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(OmidyarMap.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                        } else {
                            ActivityCompat.requestPermissions(OmidyarMap.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST);
                        }
                    }
                }
        }


    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        mapImage.setImageBitmap(bitmap);




        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOutputStream = null;
        File dir = new File(path + "/MapImagesNew/");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path + "/MapImagesNew/", "screen.jpg");
        try {
            fOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }


    }


}
