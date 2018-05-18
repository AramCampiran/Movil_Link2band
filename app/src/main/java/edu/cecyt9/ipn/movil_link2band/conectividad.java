package edu.cecyt9.ipn.movil_link2band;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link conectividad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link conectividad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class conectividad extends Fragment implements AbsListView.OnItemClickListener{

    View view;
    ArrayList<String> vinculados, cercanos;
    ListView list, list2;
    ToggleButton btnAnalizar;
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE = 0;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;


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
        btnAnalizar = view.findViewById(R.id.analizar);
        vinculados = new ArrayList<String>();
        cercanos = new ArrayList<String>();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE);
        }

        bluetooth("vinculados");

        /*String[] values2 =  {"poio", "es", "bien", "pvto"};
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text2, values2);
        //list2.setAdapter(adapter2);*/

        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    cercanos.add(String.valueOf(device.getName()));
                    System.out.println(device.getName());
                    bluetooth("cercanos");
                }
            }
        };

        btnAnalizar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                if (btnAnalizar.isChecked()) {
                    try{
                        cercanos.clear();
                    }catch (Exception e){
                        System.out.println("Exeption" + e);
                    }
                    getActivity().registerReceiver(broadcastReceiver, filter);
                    bluetoothAdapter.startDiscovery();
                } else {
                    getActivity().registerReceiver(broadcastReceiver, filter);
                    bluetoothAdapter.cancelDiscovery();
                }
            }
        });


        return view;
    }

    private void bluetooth(String tipo) {

        if (tipo.equals("vinculados")) {
            Set<BluetoothDevice> pairedDevicesList = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice pairedDevice : pairedDevicesList) {
                vinculados.add(String.valueOf(pairedDevice.getName()));
            }
            String[] values =  new String[vinculados.size()];
            for (int i = 0; i < vinculados.size(); i++) {
                values[i] = vinculados.get(i);
            }

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
            list.setAdapter(adapter);
        } else {
            String[] values =  new String[cercanos.size()];
            for (int i = 0; i < cercanos.size(); i++) {
                values[i] = cercanos.get(i);
            }
            adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
            list2.setAdapter(adapter2);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ENABLE == requestCode) {
            bluetooth("vinculados");
        }
        super.onActivityResult(requestCode, resultCode, data);
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
