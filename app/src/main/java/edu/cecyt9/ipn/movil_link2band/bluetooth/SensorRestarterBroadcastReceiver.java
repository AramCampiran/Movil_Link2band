package edu.cecyt9.ipn.movil_link2band.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops!");
        Intent service = new Intent(context, ServiceBluetooth.class);
        String direccion = intent.getStringExtra("Address");
        service.putExtra("Address", direccion);
        context.startService(service);
    }
}