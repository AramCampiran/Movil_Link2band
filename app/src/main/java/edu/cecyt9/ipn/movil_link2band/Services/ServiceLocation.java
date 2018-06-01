package edu.cecyt9.ipn.movil_link2band.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import edu.cecyt9.ipn.movil_link2band.Darclass;
import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class ServiceLocation extends Service {

    public int counter = 0;
    private LocationManager locationManager;
    private String longitud, latitud, ID;

    public ServiceLocation() {
        Log.d("Location", "Hello there!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        ID = intent.getStringExtra("ID");
        Log.d("ServiceLoc", "Ya empezó");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            //para que sirva el metodo cambia false por true
            wifiManager.setWifiEnabled(true);
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            if (networkInfo != null) {
                if (networkInfo.getTypeName().equals("MOBILE")) {
                    Log.d("ServiceLoc", "Entró a mobile");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2 * 20 * 1000, 1, locationListenerNetwork);
                } else if (networkInfo.getTypeName().equals("WIFI")) {
                    Log.d("ServiceLoc", "Entró a wifi");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 20 * 100, 1, locationListenerGPS);
                }
            }
        }
        return START_STICKY;
    }

    private LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            longitud = String.valueOf(location.getLongitude());
            latitud = String.valueOf(location.getLatitude());
            Log.d("ServiceLoc", "GPS LOC: " + latitud + ", " + longitud);
            actualizaLoc(latitud + ", " + longitud);
        }

        @Override
        public void onStatusChanged(String s, int status, Bundle bundle) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    System.out.println("AVAILABLE " + LocationProvider.AVAILABLE);
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    System.out.println("OUT_OF_SERVICE " + LocationProvider.OUT_OF_SERVICE);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    System.out.println("TEMPORARILY_UNAVAILABLE " + LocationProvider.TEMPORARILY_UNAVAILABLE);
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationListener locationListenerNetwork = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            longitud = String.valueOf(location.getLongitude());
            latitud = String.valueOf(location.getLatitude());
            Log.d("ServiceLoc", "NETWORK LOC: " + latitud + ", " + longitud);
            actualizaLoc(latitud + ", " + longitud);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void actualizaLoc(String loc) {
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente("SetLOCMovil", getApplicationContext()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                Log.d("ServiceLoc", "Mandó los datos :D");
            }
        };
        ws.SetShowDialogs(false);

        ws.execute(new String[]{"ID", "Loc"},
                new String[]{ID, loc});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service LOC On Destroy");
        locationManager.removeUpdates(locationListenerGPS);
        locationManager.removeUpdates(locationListenerNetwork);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
