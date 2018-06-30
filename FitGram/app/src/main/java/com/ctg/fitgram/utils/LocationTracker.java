package com.ctg.fitgram.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ctg.fitgram.GoogleMapActivity;
import com.ctg.fitgram.model.Data;
import com.ctg.fitgram.services.TrackingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by syeds on 1/29/2018.
 */

public class LocationTracker implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static class LocationEvent {
        public Location location;

        public LocationEvent(Location location) {
            this.location = location;
        }
    }

    private final static String TAG = LocationTracker.class.getSimpleName();
    private static final long INTERVAL = 1000 * 15;
    private static final long FASTEST_INTERVAL = 1000 * 10;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    Data data;
    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    Location lastlocation = new Location("last");

    public LocationTracker(Context context) {
        this.context = context;

        init();
    }

    private void init() {
        if (!isGooglePlayServicesAvailable(context)) return;

        this.mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void start() {
        if (mGoogleApiClient != null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            mGoogleApiClient.connect();
        }
    }

    public void stop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
            Log.d(TAG, "Location::stop");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location tracking::Permission is missing");
            Log.i(TAG, "Pls grant location permission: Settings > Apps > <This App> > Location");
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location::start");
    }

    @Override
    public void onLocationChanged(Location location)

    {

        mCurrentLocation = location;


        Log.v(TAG, "Location(" + location.getLatitude() + "," + location.getLongitude() + ")");
//for data class
        data = GoogleMapActivity.getData();
        if (data.isRunning()) {
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();

           /* if (data.isFirstTime())
            {
                lastLat = currentLat;
                lastLon = currentLon;
                data.setFirstTime(false);
            }*/

            lastLat = currentLat;
            lastLon = currentLon;

            lastlocation.setLatitude(lastLat);
            lastlocation.setLongitude(lastLon);
            double distance = lastlocation.distanceTo(location);

            if (location.getAccuracy() < distance) {
               // data.addDistance(distance);

                lastLat = currentLat;
                lastLon = currentLon;
            }

            if (location.hasSpeed())
            {
                data.setCurSpeed(location.getSpeed() * 3.6);

            }
        }
        //
        // notify UI
        EventBus.getDefault().post(new LocationEvent(mCurrentLocation));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    public static boolean isGooglePlayServicesAvailable(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }



}