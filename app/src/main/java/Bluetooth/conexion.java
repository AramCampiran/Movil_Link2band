package Bluetooth;

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

    public boolean connect(String address, UUID mUUID) {
        try {
            BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
            bTSocket = dispositivo.createRfcommSocketToServiceRecord(mUUID);
            bTSocket.connect();
        } catch(IOException e) {
            System.out.println("Could not connect: " + e.getMessage());
            try {
                bTSocket.close();
            } catch(IOException close) {
                System.out.println("Could not close connection:" + e.toString());
                return false;
            }
        }
        return true;
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
}
