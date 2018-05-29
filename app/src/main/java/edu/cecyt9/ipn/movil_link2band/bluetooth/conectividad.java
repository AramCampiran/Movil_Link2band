package edu.cecyt9.ipn.movil_link2band.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.kobjects.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link conectividad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link conectividad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class conectividad extends Fragment implements AbsListView.OnItemClickListener {

    View view;
    ArrayList<BluetoothDevice> vinculados, cercanos;
    ArrayList<String> adress;
    ListView list, list2;
    String ACTION_SETDEVICETXT = "DEVICETXT", DeviceName;
    TextView selectedDevice;
    ToggleButton btnAnalizar;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    int REQUEST_ENABLE = 123, REQUEST_PAIR = 124;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    static UUID myUUID = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public conectividad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment conectividad.
     */
    // TODO: Rename and change types and number of parameters
    public static conectividad newInstance(String param1, String param2) {
        conectividad fragment = new conectividad();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_conectividad, container, false);
        list = view.findViewById(R.id.lista);
        list2 = view.findViewById(R.id.lista2);
        list2.setTextFilterEnabled(true);
        btnAnalizar = view.findViewById(R.id.analizar);

        vinculados = new ArrayList<BluetoothDevice>();
        cercanos = new ArrayList<BluetoothDevice>();
        adress = new ArrayList<String>();


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE);
        } else {

            bluetooth("vinculados");

            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (device != null) {
                            cercanos.add(device);
                            System.out.println("Nombre " + device.getName());
                        }
                        bluetooth("cercanos");
                    }
                }
            };

            btnAnalizar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    if (btnAnalizar.isChecked()) {
                        cercanos.clear();
                        getActivity().registerReceiver(broadcastReceiver, filter);
                        bluetoothAdapter.startDiscovery();
                    } else {
                        getActivity().registerReceiver(broadcastReceiver, filter);
                        bluetoothAdapter.cancelDiscovery();
                    }
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IniciaServicio(adress.get(i), list, i);
                }
            });

            list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (isPaired(cercanos.get(i))) {
                        IniciaServicio(cercanos.get(i).getAddress(), list2, i);
                    } else {
                        byte[] pin = ("" + 0000).getBytes();
                        cercanos.get(i).setPin(pin);
                        try {
                            cercanos.get(i).setPairingConfirmation(true);
                        } catch (SecurityException e) {
                            System.out.println("Error xd: " + e.getMessage() + " | " + e.getCause());
                        }
                        System.out.println("Ganamos :D");

//                        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
//                        getContext().registerReceiver(mPairing, intent);
                    }
                }
            });
        }
        return view;
    }

    private final BroadcastReceiver mPairing = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Entro al broudcast");
//            if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(intent.getAction())) {
//                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
//                if (type == BluetoothDevice.PAIRING_VARIANT_PIN) {
//                    String x = String.valueOf(0000);
//                    device.setPin(x.getBytes());
//                    Fragment current = new conectividad();
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, current).commit();
//                } else {
//                    System.out.println("Unexpected pairing type: " + type);
//                }
//            }
        }
    };

    public int pin() {
        return getArguments().getInt("pin", -1);
    }

    public boolean isPaired(BluetoothDevice cercano) {
        for (int i = 0; i < vinculados.size(); i++) {
            if (vinculados.get(i).equals(cercano)) {
                return true;
            }
        }
        return false;
    }

    //SERVICIO

    public void IniciaServicio(String address, ListView lista, int i) {
        if (!isMyServiceRunning(ServiceBluetooth.class)) {
            selectedDevice = (TextView) lista.getChildAt(i - lista.getFirstVisiblePosition());
            DeviceName = selectedDevice.getText().toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                selectedDevice.setText(Html.fromHtml("<html><b>" + DeviceName + "</b><br>Conectando...</html>", Html.FROM_HTML_MODE_COMPACT));
            } else {
                selectedDevice.setText(Html.fromHtml("<html><b>" + DeviceName + "</b><br>Conectando...</html>"));
            }
            getContext().registerReceiver(mMessageReceiver, new IntentFilter(ACTION_SETDEVICETXT));
            DatabaseHelper DB = new DatabaseHelper(getContext());
            DB.insertaAddress(Comands.getID(), address);
            Intent ServiceIntent = new Intent(getActivity(), ServiceBluetooth.class);
            ServiceIntent.putExtra("ID", Comands.getID());
            getActivity().startService(ServiceIntent);
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_SETDEVICETXT)) {
                boolean connected = intent.getBooleanExtra("connected", false);
                if (connected) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        selectedDevice.setText(Html.fromHtml("<html><b>" + DeviceName + "</b><br>Conectado</html>", Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        selectedDevice.setText(Html.fromHtml("<html><b>" + DeviceName + "</b><br>Conectado</html>"));
                    }
                    selectedDevice.setTextColor(R.color.colorAccentII);
                } else {
                    selectedDevice.setText(DeviceName);
                    selectedDevice.setTextColor(Color.BLACK);
                }
            }
        }
    };

    public void SelectConnected(ListView lista, int i) {
//        if (list.equals(lista)) {
//            String selectedFromList = (String) list.getItemAtPosition(i);
//            Toast.makeText(getContext(), selectedFromList, T)
//        } else if (list2.equals(lista)) {
//
//        } else {
//            Toast.makeText(getContext(), "Ocurrió un error, intenta reiniciar la aplicación", Toast.LENGTH_SHORT).show();
//        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                System.out.println("Is the service running: " + true + "");
                return true;
            }
        }
        System.out.println("Is the service running: " + false + "");
        return false;
    }

    //FIN SERVICIO

    private void bluetooth(String tipo) {

        if (tipo.equals("vinculados")) {
            Set<BluetoothDevice> pairedDevicesList = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice pairedDevice : pairedDevicesList) {
                vinculados.add(pairedDevice);
                adress.add(String.valueOf(pairedDevice.getAddress()));
            }
            String[] values = new String[vinculados.size()];
            for (int i = 0; i < vinculados.size(); i++) {
                values[i] = String.valueOf(vinculados.get(i).getName());
            }

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
            list.setAdapter(adapter);
        } else {
            String[] values = new String[cercanos.size()];
            for (int i = 0; i < cercanos.size(); i++) {
                if (cercanos.get(i) != null) {
                    values[i] = cercanos.get(i).getName();
                }
            }
            adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
            list2.setAdapter(adapter2);
        }

    }

    //RESULTADO DE LA SOLICITUD DE BLUETOOTH

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE) {
            if (resultCode == Activity.RESULT_OK) {
                Fragment current = new conectividad();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, current).commit();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Aviso")
                        .setMessage("Esta aplicación requiere que el Bluetooth esté encendido para funcionar correctamente")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}