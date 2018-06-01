package edu.cecyt9.ipn.movil_link2band.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Receiver", "ACTIONS Stops!");
        context.startService(new Intent(context, ServiceActions.class));
    }
}