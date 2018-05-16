package edu.cecyt9.ipn.movil_link2band;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Usuario on 19/04/2018.
 */

public class Darclass extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        System.out.println("Activo");

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        System.out.println("NO activo");
    }
}
