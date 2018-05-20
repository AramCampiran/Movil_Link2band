package Bluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Usuario on 18/05/2018.
 */
public class conexion extends Thread{
    private BluetoothSocket bTSocket;
    boolean conect = false;

    public boolean connect(String address, UUID mUUID) {
        try {
            BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
            bTSocket = dispositivo.createRfcommSocketToServiceRecord(mUUID);
            bTSocket.connect();
            conect = true;
            return true;
        } catch(IOException e) {
            System.out.println("Could not connect: " + e.getMessage());
            try {
                bTSocket.close();
                return false;
            } catch(IOException close) {
                System.out.println("Could not close connection:" + e.toString());
                return false;
            }
        }
    }

    public boolean cancel() {
        try {
            bTSocket.close();
        } catch(IOException e) {
            System.out.println("Could not close connection:" + e.toString());
            return false;
        }
        return true;
    }

    public boolean getConection(){
        return conect;
    }
}
