package edu.cecyt9.ipn.movil_link2band.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.cecyt9.ipn.movil_link2band.Extras.ServiceLocation;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops!");
//        Intent service = new Intent(context, ServiceLocation.class);
//        String id = intent.getStringExtra("ID");
//        String modo = intent.getStringExtra("SecMode");
//        service.putExtra("ID", id);
//        service.putExtra("SecMode", modo);
//        context.startService(service);
    }
}