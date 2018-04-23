package edu.cecyt9.ipn.movil_link2band;

import android.Manifest;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecurityMechanism.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecurityMechanism#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityMechanism extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    Button btnDuracion, btnTone, btnWriteMsj, btnBloquear, btnLocalizar;
    TextView msj;
    String[] DuraOptions = {"15 segundos", "30 segundos", "1 minuto", "5 minutos", "10 minutos"};
    RadioButton rbTotal, rbParcial;
    Switch swBloqueo;

    int Selected = 0;
    final int RQS_RINGTONEPICKER = 1;
    Uri uriRingTone;
    Ringtone ringtone;

    DevicePolicyManager devicePolicyManager;
    ComponentName component;
    int REQUEST_ENABLE = 0;

    LocationManager locationManager;
    double longitudGPS, latitudGPS;
    String txt;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SecurityMechanism() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityMechanism.
     */
    // TODO: Rename and change types and number of parameters
    public static SecurityMechanism newInstance(String param1, String param2) {
        SecurityMechanism fragment = new SecurityMechanism();
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
        view = inflater.inflate(R.layout.fragment_security_mechanism, container, false);
        btnDuracion = view.findViewById(R.id.duracion);
        btnDuracion.setOnClickListener(this);
        rbTotal = view.findViewById(R.id.total);
        rbParcial = view.findViewById(R.id.partial);
        swBloqueo = view.findViewById(R.id.blockMode);
        swBloqueo.setOnClickListener(this);

        msj = view.findViewById(R.id.msjInScreen);

        btnTone = view.findViewById(R.id.tone);
        btnTone.setOnClickListener(this);
        btnWriteMsj = view.findViewById(R.id.WriteMsj);
        btnWriteMsj.setOnClickListener(this);
        btnBloquear = view.findViewById(R.id.bloquear);
        btnBloquear.setOnClickListener(this);
        uriRingTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btnLocalizar = view.findViewById(R.id.localizar);
        btnLocalizar.setOnClickListener(this);


        return view;
    }

    // TODO: Rename method, update argument an"ld hook method into UI event
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
    public void onClick(View v) {
        if (v.getId() == btnDuracion.getId()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("btnDuracion de alarma")
                    .setSingleChoiceItems(DuraOptions, Selected, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Selected = i;
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            btnDuracion.setText(DuraOptions[Selected]);
                        }
                    }).show();
        } else if (v.getId() == swBloqueo.getId()) {
            if (swBloqueo.isChecked()) {
                rbParcial.setVisibility(View.VISIBLE);
                rbTotal.setVisibility(View.VISIBLE);
            } else {
                rbParcial.setVisibility(View.INVISIBLE);
                rbTotal.setVisibility(View.INVISIBLE);
            }
        } else if (v.getId() == btnTone.getId()) {
            if (ringtone != null) {
                ringtone.stop();
            }
            ringtone = RingtoneManager.getRingtone(getContext(), uriRingTone);
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            startActivityForResult(intent, RQS_RINGTONEPICKER);
        } else if (v.getId() == btnWriteMsj.getId()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.alert, null);
            alert.setTitle("Mensaje en pantalla")
                    .setView(view)
                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText txt = view.findViewById(R.id.msj);
                            if (!txt.getText().toString().equals("")) {
                                msj.setText("Mensaje: " + txt.getText().toString());
                            } else {
                                msj.setText("Debes de escribir un mensaje");
                            }
                        }
                    })
                    .setNegativeButton("cancelar", null).show();
        } else if (v.getId() == btnBloquear.getId()) {
            devicePolicyManager = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
            component = new ComponentName(getContext(), Darclass.class);

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("Esta seguro de bloquer")
                    .setPositiveButton("si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!devicePolicyManager.isAdminActive(component)) {
                                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
                                startActivityForResult(intent, REQUEST_ENABLE);
                            } else {
                                devicePolicyManager.lockNow();
                            }
                        }
                    })
                    .setNegativeButton("nel", null).show();
        } else if (v.getId() == btnLocalizar.getId()) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("activar ubicacion")
                        .setMessage("su ubicacion esta desactivada \n por favor active su ubicacion")
                        .setPositiveButton("Configurar ubicacion", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            } else {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } else {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2*20*1000, 10, locationListenerGPS);
                }
            }
        }
    }
    private LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
           AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
           alert.setTitle("Localizacion")
           .setMessage("Latitud: " + location.getLatitude() +"\n"+ " Longitud: " + location.getLongitude())
           .setPositiveButton("ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   locationManager.removeUpdates(locationListenerGPS);
               }
           }).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void setLocal(String txt){
        System.out.println(txt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQS_RINGTONEPICKER && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtone = RingtoneManager.getRingtone(getContext(), uri);
            btnTone.setText(ringtone.getTitle(getContext()));
        }else if (REQUEST_ENABLE == requestCode){
            super.onActivityResult(requestCode, resultCode, data);
        }
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
