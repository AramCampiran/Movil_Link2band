package edu.cecyt9.ipn.movil_link2band.Extras;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
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

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;

public class ServiceLocation extends Service {

    private LocationManager locationManager;
    private String longitud, latitud, SecMode, ID;
    private boolean Restart = true;

    public ServiceLocation() {
        Log.d("Location", "Hello there!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("ya empezó");
        SecMode = intent.getStringExtra("SecMode");
        ID = intent.getStringExtra("ID");
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            //para que sirva el metodo cambia false por true
            wifiManager.setWifiEnabled(true);
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
//            return;
        }
        if (networkInfo == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("Ups! ha ocurrido un error")
                    .setMessage("Por favor active conexion de datos mobiles o wifi")
                    .setPositiveButton("Aceptar", null)
                    .show();
        } else if (networkInfo.getTypeName().equals("WIFI")) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 20 * 100, 10, locationListenerGPS);
//            dialog = new ProgressDialog(getApplicationContext());
//            dialog.setMessage("Buscando localizacion");
//            dialog.setMax(300);
//            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialog.dismiss();
//                    locationManager.removeUpdates(locationListenerGPS);
//                    locationManager.removeUpdates(locationListenerNetwork);
//                }
//            });
//            dialog.show();
        } else if (networkInfo.getTypeName().equals("MOBILE")) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2 * 20 * 1000, 10, locationListenerNetwork);
//            dialog = ProgressDialog.show(getApplicationContext(), "", "Buscando localizacion");
        }
        return START_STICKY;
    }

    private LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            longitud = String.valueOf(location.getLongitude());
            latitud = String.valueOf(location.getLatitude());
            System.out.println("Latitud: " + latitud + "\n" + " Longitud: " + longitud);
//            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//            alert.setTitle("Localizacion")
//                    .setMessage("Latitud: " + latitud + "\n" + " Longitud: " + longitud)
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            locationManager.removeUpdates(locationListenerGPS);
//                        }
//                    }).show();
//            dialog.dismiss();
            actualizaLoc(latitud + ", " + longitud, "Desbloqueado", SecMode);
            DatabaseHelper DB = new DatabaseHelper(getApplicationContext());
            DB.actualiza(Comands.getID(), latitud + ", " + longitud);
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
            System.out.println("Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude());
//            AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
//            alert.setTitle("Localizacion")
//                    .setMessage("Latitud: " + latitud + "\n" + " Longitud: " + longitud)
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            locationManager.removeUpdates(locationListenerNetwork);
//                        }
//                    }).show();
//            dialog.dismiss();
            actualizaLoc(latitud + ", " + longitud, "Desbloqueado", SecMode);
            DatabaseHelper DB = new DatabaseHelper(getApplicationContext());
            DB.actualiza(Comands.getID(), latitud + ", " + longitud);
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

    private void actualizaLoc(String loc, String stat, String mode) {
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente("SetDatosMovil", getApplicationContext()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setMessage("Datos enviados")
//                        .setPositiveButton("Ok", null).show();
//                System.out.println("Datos enviados");
            }
        };
        ws.execute(new String[]{"ID", "Loc", "Status", "secureMode"},
                new String[]{ID, loc, stat, mode});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service LOC On Destroy");
        if (Restart) {
            Intent broadcastIntent = new Intent(".ActivityRecognition.RestartSensor");
            broadcastIntent.putExtra("ID", ID);
            broadcastIntent.putExtra("SecMode", SecMode);
            sendBroadcast(broadcastIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
