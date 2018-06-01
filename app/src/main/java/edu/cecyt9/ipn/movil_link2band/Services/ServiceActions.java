package edu.cecyt9.ipn.movil_link2band.Services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
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
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import edu.cecyt9.ipn.movil_link2band.Darclass;
import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class ServiceActions extends Service {

    boolean Restart = true;
    int counter = 0, DurationSeconds = 0;
    String ACTION_LOGOUT = "LogOut";
    Ringtone ringtone;
    DatabaseHelper DB;
    String ID;

    public ServiceActions() {
        System.out.println("Hey! Actions!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        DB = new DatabaseHelper(getApplicationContext());
        ID = DB.selectIDs();
        getApplicationContext().registerReceiver(LogOutReceiver, new IntentFilter(ACTION_LOGOUT));
        SendInfo();
        SearchActions();
        return START_STICKY;
    }

    private void SendInfo() {
        DB.consulta(ID);
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente("SetDatosMovil", getApplicationContext()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                Log.d("ServiceACTIONS", "Mandó los datos :D");
            }
        };
        ws.SetShowDialogs(false);
        String modo = "";
        if (Comands.getSMODE().equals("true")) modo = "Activado";
        else if (Comands.getSMODE().equals("false")) modo = "Desactivado";
        String status = "";
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager.getPasswordMinimumLength(new ComponentName(getApplicationContext(), Darclass.class)) > 0) {
            status = "Bloqueado";
        } else {
            status = "Desbloqueado";
        }
        ws.execute(new String[]{"ID", "Status", "secureMode"},
                new String[]{ID, status, modo});
    }

    private void SearchActions() {
        DB.consulta(ID);
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente("GetActionsMovil", getApplicationContext()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                Log.d("ServiceACTIONS", "Recibió los datos :D");
                String[] data = super.Results;
                if (!data[0].isEmpty()) {
                    DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName demoDeviceAdmin = new ComponentName(getApplicationContext(), Darclass.class);
                    if (Boolean.parseBoolean(data[0])) {
                        devicePolicyManager.setPasswordQuality(demoDeviceAdmin, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
                        devicePolicyManager.setPasswordMinimumLength(demoDeviceAdmin, 4);
                        boolean result = devicePolicyManager.resetPassword(data[2], DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
                        devicePolicyManager.lockNow();
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(Intent.ACTION_USER_PRESENT);
                    } else {
                        devicePolicyManager.setPasswordQuality(demoDeviceAdmin, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
                        devicePolicyManager.setPasswordMinimumLength(demoDeviceAdmin, 0);
                        boolean result = devicePolicyManager.resetPassword("", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(Intent.ACTION_USER_PRESENT);
                    }
                }
                if (!data[1].isEmpty() && Boolean.parseBoolean(data[1])) {
                    AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                    try {
                        Uri uri = Uri.parse(Comands.getURISTRING());
                        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                        if (ringtone != null) {
                            if (ringtone.isPlaying())
                                ringtone.stop();
                            ringtone.play();
                            DurationSeconds = Comands.getDURATION_SECONDS();
                            stoptimertask();
                            startTimer();
                        }
                    } catch (Exception e) {
                    }
                }
                if (data[1].isEmpty()) {
                    Restart();
                }
            }
        };
        ws.SetShowDialogs(false);
        ws.execute(new String[]{"ID"},
                new String[]{ID});
    }


    private void Restart() {
        stoptimertask();
        SendInfo();
        SearchActions();
    }

    public BroadcastReceiver LogOutReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_LOGOUT)) {
                Restart = false;
                stopSelf();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("ACTIONS On Destroy");
        if (Restart) {
            Intent broadcastIntent = new Intent(".ActivityRecognition.RestartSensor");
            sendBroadcast(broadcastIntent);
        }
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
                Log.i("TIMER", "ACTIONS: " + (counter++));
                if (counter >= DurationSeconds) {
                    if (ringtone != null) {
                        if (ringtone.isPlaying()) {
                            ringtone.stop();
                        }
                    }
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
