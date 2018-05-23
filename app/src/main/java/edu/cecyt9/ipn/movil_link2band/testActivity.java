package edu.cecyt9.ipn.movil_link2band;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Set;

import edu.cecyt9.ipn.movil_link2band.bluetooth.SensorRestarterBroadcastReceiver;
import edu.cecyt9.ipn.movil_link2band.bluetooth.ServiceBluetooth;
import edu.cecyt9.ipn.movil_link2band.bluetooth.conectividad;

public class testActivity extends AppCompatActivity {

    LinearLayout LL;
    BluetoothAdapter bluetoothAdapter;
    Intent mServiceIntent;
    private ServiceBluetooth mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LL = findViewById(R.id.LL);
        ctx = this;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        } else {
            Set<BluetoothDevice> pairedDevicesList = bluetoothAdapter.getBondedDevices();
            for (final BluetoothDevice pairedDevice : pairedDevicesList) {
                Button btn = new Button(this);
                btn.setText(pairedDevice.getName());
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSensorService = new ServiceBluetooth(getCtx());
                        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            startService(mServiceIntent);
                        }
                    }
                });
                LL.addView(btn);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                System.out.println("Is the service running: " + true + "");
                return true;
            }
        }
        System.out.println("Is the service running: " + false + "");
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                new Intent(this, testActivity.class);
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        System.out.println("OnDestroy!");
        super.onDestroy();
    }
}
