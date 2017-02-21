package com.sayone.omidyar.view;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sayone on 16/2/17.
 */

public class GpsTrackerService extends Service {
    private static final String TAG = "GPS_TRACKER_SERVICE";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;
    private final IBinder mBinder = new GpsTrackerServiceBinder();
    private Location mLastLocation;
    private int mStatus = 1;
    private int isGPSEnabled = 1;

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            isGPSEnabled = 0;
            Toast.makeText(getApplicationContext(), "GPS disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            isGPSEnabled = 1;
            Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            mStatus = status;
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER)
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public class GpsTrackerServiceBinder extends Binder {
        GpsTrackerService getService() {
            return GpsTrackerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Location getLocation() {
        if (mStatus == LocationProvider.OUT_OF_SERVICE) {
            Toast.makeText(getApplicationContext(), "GPS not available", Toast.LENGTH_SHORT).show();
        } else if (mStatus == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Toast.makeText(getApplicationContext(), "Searching for GPS", Toast.LENGTH_SHORT).show();
        } else if (mStatus == LocationProvider.AVAILABLE) {

        }
        if (isGPSEnabled == 0) {
            Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
            return null;
        }
        return mLastLocation;
    }

}
