package com.example.regis.servicelocation;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MyLocationTracker extends Service implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, TransferData {
    private String Tag = "MyLocationTracker";
    private boolean flag;
    private static final int FINE_LOCATION = 1;
    private static final int COARSE_LOCATION = 2;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private boolean grantedPermission = false;
    private int requestCode;
    // private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest = new LocationRequest();
        LocationRequestConfiguration();
        new InternetAsync(this).execute();

    }

    private void LocationRequestConfiguration() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            Toast.makeText(getApplicationContext(), "Setting it to network ", Toast.LENGTH_LONG).show();
        } else {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Toast.makeText(getApplicationContext(), "Setting it to gps ", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getApplicationContext(), "Location Service is set to " + locationRequest.getPriority(),
                Toast.LENGTH_LONG).show();
    }

    public NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public void checkConnection() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            String connectionMode = networkInfo.getTypeName();
            if (flag) {
                Toast.makeText(getApplicationContext(), "Connected to " + connectionMode + "\nInternet Working", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Connected to " + connectionMode + "Internet Not working", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Network Not detected", Toast.LENGTH_LONG).show();
        }
    }


    public void locationRequestUpdate() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.v("Request ", "rejected");
                Log.v("android version", " " + Build.VERSION.SDK_INT);
                Toast.makeText(getApplicationContext(), "Request not granted ", Toast.LENGTH_LONG).show();

                return;
            } else {
                Log.v("Request ", "accepted");
            }
        }

        Log.v("my_version ", "" + Build.VERSION.SDK_INT);

        Toast.makeText(getApplicationContext(), "\"build inside locationRequestUpdate\"", Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        googleApiClient.connect();
        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (grantedPermission) {
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequestUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Connection Suspended ", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(Tag, "Connection failed" + connectionResult.getErrorMessage());
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(), "hello am in on location update", Toast.LENGTH_SHORT).show();
        double lat, lon;
        lat = location.getLatitude();
        lon = location.getLongitude();
        Toast.makeText(getApplicationContext(), "Updated Location \n" +
                "Latitude:" + lat + "\nLongitude:" + lon, Toast.LENGTH_LONG).show();
        Log.v("loctaion priority ", "" + locationRequest.getPriority());
        Toast.makeText(getApplicationContext(), "Location prority " + locationRequest.getPriority(), Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] grantResults) {

        switch (requestCode) {
            case FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermission = true;
                    Toast.makeText(getApplicationContext(), "granted", Toast.LENGTH_SHORT).show();
                } else {
                    grantedPermission = false;
                    String req = getResources().getString(R.string.request_not_granted);
                    Toast.makeText(getApplicationContext(), req, Toast.LENGTH_SHORT).show();
                }
                break;
            case COARSE_LOCATION:

                break;
        }

    }

    @Override
    public void setData(Boolean b) {
        flag = b;
        Log.v("flag ", "g" + flag);
        checkConnection();
    }
}