package edu.cecyt9.ipn.movil_link2band.bluetooth;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import edu.cecyt9.ipn.movil_link2band.R;

/**
 * Created by fabio on 30/01/2016.
 */
public class ServiceBluetooth extends Service {
    public int counter = 0;
    private String Address;
    private boolean Restart;

    public ServiceBluetooth() {
        super();
        Log.i("HERE", "here I am!");
        Restart = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Address = intent.getStringExtra("Address");
        BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Address);
        try {
            BluetoothSocket bTSocket = dispositivo.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            if (!bTSocket.isConnected()) {
                bTSocket.connect();
                IntentFilter filter = new IntentFilter();
//                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                this.registerReceiver(mReceiver, filter);

                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Link2Band")
                        .setContentText("Pulsera conectada").build();
                startForeground(1303, notification);
                startTimer();
                Toast.makeText(getApplicationContext(), "Conexión exitosa", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    bTSocket.close();
                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        return START_STICKY;
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (device.getName().equals("L2B BAND")) {
                if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Pulsera desconectada\nIniciando mecanismos...", Toast.LENGTH_SHORT).show();
                    Restart = false;
                    stopSelf();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service On Destroy");
        if (Restart) {
            Intent broadcastIntent = new Intent(".ActivityRecognition.RestartSensor");
            broadcastIntent.putExtra("Address", Address);
            sendBroadcast(broadcastIntent);
        }
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}