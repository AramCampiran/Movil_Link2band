package edu.cecyt9.ipn.movil_link2band.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import edu.cecyt9.ipn.movil_link2band.Darclass;
import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.R;
import edu.cecyt9.ipn.movil_link2band.principal;

/**
 * Created by fabio on 30/01/2016.
 */
public class ServiceBluetooth extends Service {
    private DatabaseHelper DB;
    public int counter = 0, notiID = 1303, DurationSeconds = 0;
    private String Address, UriString, Msj, ID, SecMode;
    private Intent serviceloc;
    private boolean Restart;
    private Ringtone ringtone;
    private Notification notification;

    public ServiceBluetooth() {
        super();
        Log.i("HERE", "here I am!");
        Restart = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        ID = intent.getStringExtra("ID");
        DB = new DatabaseHelper(getApplicationContext());
        DB.consulta(ID);
        Address = Comands.getADDRESS();
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
                this.registerReceiver(BTReceiver, filter);

                notification = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Link2Band")
                        .setContentText("Pulsera conectada")
                        .setContentIntent(
                                PendingIntent.getActivity(getApplicationContext(), 10,
                                        new Intent(getApplicationContext(), principal.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                                        0)
                        ).build();
                startForeground(notiID, notification);
                Toast.makeText(getApplicationContext(), "Conexión exitosa", Toast.LENGTH_SHORT).show();
                Intent intento = new Intent("DEVICETXT");
                intento.putExtra("connected", true);
                sendBroadcast(intento);
            } else {
                try {
                    bTSocket.close();
                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "No se pudo establecer la conexión", Toast.LENGTH_SHORT).show();
            Intent intento = new Intent("DEVICETXT");
            intento.putExtra("connected", false);
            sendBroadcast(intento);
            stopSelf();
        }
        return START_STICKY;
    }

    final BroadcastReceiver BTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Intent intento = new Intent("DISCONNECTDEVICETXT");
                sendBroadcast(intento);
                if (Boolean.valueOf(Comands.getSMODE())) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getName().equals("L2B BAND")) {
                        DB.consulta(ID);
                        SecMode = Comands.getSMODE();
                        UriString = Comands.getURISTRING();
                        Msj = Comands.getMSJ();
                        DurationSeconds = Comands.getDURATION_SECONDS();
                        Bloqueo();
                        Toast.makeText(getApplicationContext(), "Pulsera desconectada\nIniciando mecanismos...", Toast.LENGTH_SHORT).show();
                        serviceloc = new Intent(getApplicationContext(), ServiceLocation.class);
                        serviceloc.putExtra("ID", ID);
                        serviceloc.putExtra("SecMode", SecMode);
                        startService(serviceloc);
                    }
                } else {
                    stopSelf();
                }
            }
        }
    };

    @SuppressLint("NewApi")
    private void Bloqueo() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        try {
            Uri uri = Uri.parse(UriString);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
            ringtone.play();
            startTimer();
            devicePolicyManager.lockNow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                devicePolicyManager.setDeviceOwnerLockScreenInfo(new ComponentName(getApplicationContext(), Darclass.class), Msj);
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_PRESENT);
            this.registerReceiver(UnlockReceiver, filter);
            stopForeground(true);
            notification = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Link2Band")
                    .setContentText("Pulsera desconectada")
                    .setSubText("Los mecanismos de seguridad se ejecutaron correctamente")
                    .setContentIntent(
                            PendingIntent.getActivity(getApplicationContext(), 10,
                                    new Intent(getApplicationContext(), principal.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                                    0)
                    ).build();
            startForeground(notiID, notification);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Hubo un problema para ejecutar los mecanismos", Toast.LENGTH_SHORT).show();
        }
    }

    final BroadcastReceiver UnlockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_USER_PRESENT.equals(action)) {
                if (ringtone != null) {
                    if (ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                }
//                Restart = false;
                try {
                    stopService(serviceloc);
                } catch (RuntimeException e) {
                    System.out.println("El servicio no está corriendo");
                }
                stopSelf();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service On Destroy");
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        counter = 0;
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
                Log.i("TIMER", "BT: " + (counter++));
                if (counter >= DurationSeconds) {
                    if (ringtone != null) {
                        if (ringtone.isPlaying()) {
                            ringtone.stop();
                        }
                    }
                    stoptimertask();
                }
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