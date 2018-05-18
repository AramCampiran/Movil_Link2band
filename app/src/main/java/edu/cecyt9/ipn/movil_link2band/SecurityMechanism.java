package edu.cecyt9.ipn.movil_link2band;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecurityMechanism.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecurityMechanism#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityMechanism extends Fragment implements View.OnClickListener, conectividad.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    Button btnDuracion, btnTone, btnWriteMsj, btnBloquear, btnLocalizar;
    TextView msj;
    String[] DuraOptions = {"15 segundos", "30 segundos", "1 minuto", "5 minutos", "10 minutos"};
    RadioButton rbTotal, rbParcial;
    Switch swBloqueo, swSecMod;

    static int Selected = 0;
    final int RQS_RINGTONEPICKER = 1;
    Uri uriRingTone;
    Ringtone ringtone;
    static String tone;

    DevicePolicyManager devicePolicyManager;
    ComponentName component;
    int REQUEST_ENABLE = 0;

    LocationManager locationManager;
    ProgressDialog dialog;
    String longitud, latitud;
    String SecMode;
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
        swSecMod = view.findViewById(R.id.saveMode);
        swSecMod.setOnClickListener(this);
        btnTone = view.findViewById(R.id.tone);
        btnTone.setOnClickListener(this);
        btnWriteMsj = view.findViewById(R.id.WriteMsj);
        btnWriteMsj.setOnClickListener(this);
        btnBloquear = view.findViewById(R.id.bloquear);
        btnBloquear.setOnClickListener(this);

        uriRingTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        msj = view.findViewById(R.id.msjInScreen);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btnLocalizar = view.findViewById(R.id.localizar);
        btnLocalizar.setOnClickListener(this);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("El sistema GPS esta desactivado \n ¿Desea activarlo?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

        }
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
        }

        if (Comands.getSMODE() != null) {
            swSecMod.setChecked(Boolean.parseBoolean(Comands.getSMODE()));
            swBloqueo.setChecked(Boolean.parseBoolean(Comands.getBLOCK()));
            rbParcial.setChecked(Boolean.parseBoolean(Comands.getPARBLOCK()));
            rbTotal.setChecked(Boolean.parseBoolean(Comands.getTOTBLOCK()));
            btnDuracion.setText(Comands.getDURATION());
            btnTone.setText(Comands.getTONE());
            msj.setText(Comands.getMSJ());
            tone = Comands.getTONE();
        }

        visibilidad();

        final NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_mechanism);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("¡no hay conexion!")
                    .setMessage("al parecer no estas conectado a nunguna pulsera \n ¿Quieres conectarte?")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Fragment fragment = new conectividad();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentPrincipal, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            toolbar.setTitle("Conectividad");
                            navigationView.setCheckedItem(R.id.nav_blue);

                        }
                    })
                    .setNegativeButton("no", null)
                    .show();
        }
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
    public void onDestroy() {
        super.onDestroy();
        DatabaseHelper DB = new DatabaseHelper(getContext());
        DB.insertaMecanismos(Comands.getID(), String.valueOf(swSecMod.isChecked()), String.valueOf(swBloqueo.isChecked()),
                String.valueOf(rbParcial.isChecked()), String.valueOf(rbTotal.isChecked()), DuraOptions[Selected], tone,
                msj.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHelper DB = new DatabaseHelper(getContext());
        DB.insertaMecanismos(Comands.getID(), String.valueOf(swSecMod.isChecked()), String.valueOf(swBloqueo.isChecked()),
                String.valueOf(rbParcial.isChecked()), String.valueOf(rbTotal.isChecked()), DuraOptions[Selected], tone,
                msj.getText().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        } else if (v.getId() == swSecMod.getId()) {
            visibilidad();

        } else if (v.getId() == swBloqueo.getId()) {
            if (swBloqueo.isChecked()) {
                rbParcial.setVisibility(View.VISIBLE);
                rbTotal.setVisibility(View.VISIBLE);
            } else {
                rbParcial.setVisibility(View.GONE);
                rbTotal.setVisibility(View.GONE);
                rbParcial.setChecked(false);
                rbTotal.setChecked(false);
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
            final EditText txt = view.findViewById(R.id.msj);
            final View view = inflater.inflate(R.layout.alert, null);
            alert.setTitle("Mensaje en pantalla")
                    .setView(view)
                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
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
            boolean active = devicePolicyManager.isAdminActive(component);

            UserManager userManager = (UserManager) getContext().getSystemService(Context.USER_SERVICE);
            UserHandle io = android.os.Process.myUserHandle();
            long serialNumber = userManager.getSerialNumberForUser(io);
            System.out.println(serialNumber);

            if (!active) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
                //intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "dame permiso prro");
                startActivityForResult(intent, REQUEST_ENABLE);
            } else {
                try {
                    ringtone.play();
                    devicePolicyManager.lockNow();
                } catch (Exception e) {
                    System.out.println("No ps no se pudo: " + e);
                }
            }

        } else if (v.getId() == btnLocalizar.getId()) {
            System.out.println("Loc: " + Comands.getLOC());
            if (!Comands.getLOC().equals("null")) {
                actualizaLoc(Comands.getLOC(), "Bloqueado", SecMode);
            } else {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (!wifiManager.isWifiEnabled()) {
                    //para que sirva el metodo cambia false por true
                    wifiManager.setWifiEnabled(true);
                }

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
                if (networkInfo == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Ups! ha ocurrido un error")
                            .setMessage("Por favor active conexion de datos mobiles o wifi")
                            .setPositiveButton("Aceptar", null)
                            .show();
                } else if (networkInfo.getTypeName().equals("WIFI")) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 20 * 100, 10, locationListenerGPS);
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Buscando localizacion");
                    dialog.setMax(300);
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            locationManager.removeUpdates(locationListenerGPS);
                            locationManager.removeUpdates(locationListenerNetwork);
                        }
                    });
                    dialog.show();
                    /*actualizaLoc( "19.4535061,-99.1752977", "Desbloqueado", SecMode);*/

                } else if (networkInfo.getTypeName().equals("MOBILE")) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2 * 20 * 1000, 10, locationListenerNetwork);
                    dialog = ProgressDialog.show(getActivity(), "", "Buscando localizacion");
                }

            }
        }
    }

    private void visibilidad() {
        if (swSecMod.isChecked()) {
            SecMode = "Activado";
            swBloqueo.setEnabled(true);
            btnDuracion.setEnabled(true);
            btnTone.setEnabled(true);
            btnWriteMsj.setEnabled(true);
            btnLocalizar.setVisibility(View.VISIBLE);
            btnBloquear.setVisibility(View.VISIBLE);
            rbParcial.setEnabled(true);
            rbTotal.setEnabled(true);
        } else {
            SecMode = "Desactivado";
            swBloqueo.setEnabled(false);
            btnDuracion.setEnabled(false);
            btnTone.setEnabled(false);
            btnWriteMsj.setEnabled(false);
            btnBloquear.setVisibility(View.GONE);
            rbParcial.setEnabled(false);
            rbTotal.setEnabled(false);
        }

        if (swBloqueo.isChecked()) {
            rbParcial.setVisibility(View.VISIBLE);
            rbTotal.setVisibility(View.VISIBLE);
        } else {
            rbParcial.setVisibility(View.GONE);
            rbTotal.setVisibility(View.GONE);
            rbParcial.setChecked(false);
            rbTotal.setChecked(false);

        }
    }

    private void actualizaLoc(String loc, String stat, String mode) {
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente("SetDatosMovil", getActivity()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Datos enviados")
                            .setPositiveButton("Ok", null).show();
                    System.out.println("Datos enviados");
            }
        };
        ws.execute(new String[]{"ID", "Loc", "Status", "secureMode"},
                new String[]{Comands.getID(), loc, stat, mode});
    }

    private LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            longitud = String.valueOf(location.getLongitude());
            latitud = String.valueOf(location.getLatitude());
            System.out.println("Latitud: " + latitud + "\n" + " Longitud: " + longitud);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Localizacion")
                    .setMessage("Latitud: " + latitud + "\n" + " Longitud: " + longitud)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            locationManager.removeUpdates(locationListenerGPS);
                        }
                    }).show();
            dialog.dismiss();
            actualizaLoc(latitud + ", " + longitud, "Desbloqueado", SecMode);
            DatabaseHelper DB = new DatabaseHelper(getActivity());
            DB.actualiza(Comands.getID(), latitud + ", " + longitud);
        }

        @Override
        public void onStatusChanged(String s, int status, Bundle bundle) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    System.out.println("AVAILABLE " + LocationProvider.AVAILABLE);
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    System.out.println("OUT_OF_SERVICE " + LocationProvider.OUT_OF_SERVICE);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    System.out.println("TEMPORARILY_UNAVAILABLE " + LocationProvider.TEMPORARILY_UNAVAILABLE);
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationListener locationListenerNetwork = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            longitud = String.valueOf(location.getLongitude());
            latitud = String.valueOf(location.getLatitude());
            System.out.println("Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude());
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Localizacion")
                    .setMessage("Latitud: " + latitud + "\n" + " Longitud: " + longitud)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            locationManager.removeUpdates(locationListenerNetwork);
                        }
                    }).show();
            dialog.dismiss();
            DatabaseHelper DB = new DatabaseHelper(getActivity());
            DB.actualiza(Comands.getID(), latitud + ", " + longitud);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQS_RINGTONEPICKER && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtone = RingtoneManager.getRingtone(getContext(), uri);
            btnTone.setText(ringtone.getTitle(getContext()));
            tone = ringtone.getTitle(getContext());
        } else if (REQUEST_ENABLE == requestCode) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        if (ringtone != null) {
            if (ringtone.isPlaying()) {
                ringtone.stop();
            }
        }
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
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
