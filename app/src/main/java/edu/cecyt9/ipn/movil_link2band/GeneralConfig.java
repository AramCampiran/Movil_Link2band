package edu.cecyt9.ipn.movil_link2band;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.device.DeviceName;

import java.lang.reflect.Type;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeneralConfig.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeneralConfig#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralConfig extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Intent intent;

    View view;
    Button btnDelete;
    ImageButton editNom, editMail, editPass;
    TextView txtName, txtMail, txtPass;

    Long id;
    private OnFragmentInteractionListener mListener;

    DatabaseHelper DB;

    public GeneralConfig() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneralConfig.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralConfig newInstance(String param1, String param2) {
        GeneralConfig fragment = new GeneralConfig();
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
        view = inflater.inflate(R.layout.fragment_general_config, container, false);
        btnDelete = view.findViewById(R.id.delete);
        btnDelete.setOnClickListener(this);
        DB = new DatabaseHelper(getActivity());

        editNom = view.findViewById(R.id.editNom);
        editNom.setOnClickListener(this);
        editMail = view.findViewById(R.id.editMail);
        editMail.setOnClickListener(this);
        editPass = view.findViewById(R.id.editPass);
        editPass.setOnClickListener(this);

        txtName = view.findViewById(R.id.name);

        txtMail = view.findViewById(R.id.mail);

        txtPass = view.findViewById(R.id.password);

        DatabaseHelper DB = new DatabaseHelper(getActivity());
        if (!consulta(Comands.getID())) {
            txtName.setText(Comands.getNOM());
            txtPass.setText(Comands.getPASS());
            txtMail.setText(Comands.getMAIL());
        }

        return view;

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
    public void onClick(View v) {
        final String[] contra = new String[1];
        if (v.getId() == btnDelete.getId()) {
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View vi = inflater.inflate(R.layout.alert, null);
            final EditText txt = vi.findViewById(R.id.msj);
            txt.setHint("contraseña actual");
            txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Verifica contraseña")
                    .setView(vi)
                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!txt.getText().toString().equals("")) {
                                contra[0] = txt.getText().toString();
                                elimina(contra[0]);
                            } else {
                                contra[0] = "";
                            }
                        }
                    })
                    .setNegativeButton("cancelar", null).show();

        } else if (v.getId() == editNom.getId()) {
            String temp = txtName.getText().toString();
            txtName.setText(alert("Nuevo nombre", "Nombre"));
            if (txtName.getText().toString().equals("")) {
                txtName.setText(temp);
            }
        } else if (v.getId() == editMail.getId()) {
            String temp = txtMail.getText().toString();
            txtMail.setText(alert("Nuevo correo", "Correo"));
            if (txtMail.getText().toString().equals("")) {
                txtMail.setText(temp);
            }
        } else if (v.getId() == editPass.getId()) {
            String temp = txtPass.getText().toString();
            txtPass.setText(alert("Nueva contraseña", "Pass"));
            if (txtPass.getText().toString().equals("")) {
                txtPass.setText(temp);
            }
        }
    }

    private String alert(String hint, final String campo) {
        final String[] cambio = {""};
        LayoutInflater factory = getActivity().getLayoutInflater();

        View nView = factory.inflate(R.layout.alert, null);

        final EditText txt = nView.findViewById(R.id.msj);
        final EditText nuevo = nView.findViewById(R.id.cambio);

        txt.setHint("Contraseña actual");
        txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txt.setSingleLine(true);
        nuevo.setVisibility(View.VISIBLE);
        nuevo.setHint(hint);
        nuevo.setSingleLine(true);

        if (hint.equals("Nueva contraseña"))
            nuevo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else
            nuevo.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(nView)
                .setTitle("Guardar cambios")
                .setMessage("Por favor escribe tu contraseña para verificar tu identidad")
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!nuevo.getText().toString().equals("") && !txt.getText().toString().equals("")) {
                            saveChanges(nuevo.getText().toString(), txt.getText().toString(), campo);
                            cambio[0] = nuevo.getText().toString();
                        }
                    }
                })
                .setNegativeButton("cancelar", null)
                .show();
        return cambio[0];
    }

    private void saveChanges(String parameter, String verificationPass, String paramName) {
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.CambioMethod), getActivity()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                if (Boolean.parseBoolean(super.Results[0])) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Editar datos")
                            .setMessage(super.Results[1])
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseHelper DB = new DatabaseHelper(getActivity());

                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    DB.bajaUSR(Comands.getID());
                                }
                            }).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Datos inválidos")
                            .setMessage(super.Results[1])
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                }
            }
        };
        ws.execute(new String[]{"ID", "Parameter", "VerificationPass", "ParamName"},
                new String[]{Comands.getID(), parameter, verificationPass, paramName});
    }

    private void elimina(String contra) {
        final DatabaseHelper DB = new DatabaseHelper(getActivity());
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.BajaMethod), getActivity()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                if (Boolean.parseBoolean(super.Results[0])) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Eliminar usuario")
                            .setMessage(super.Results[1])
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    DB.bajaUSR(Comands.getID());
                                }
                            }).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Datos inválidos")
                            .setMessage(super.Results[1])
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                }
            }
        };
        ws.execute(new String[]{"ID", "Pass"},
                new String[]{Comands.getID(), contra});

    }

    private boolean consulta(String id) {
        final Boolean[] correct = {false};
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.ConsultaMethod), getActivity()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                if (Boolean.parseBoolean(super.Results[0])) {
                    Comands.setNOM(super.Results[1]);
                    Comands.setPASS(super.Results[2]);
                    Comands.setMAIL(super.Results[4]);
                    correct[0] = true;
                } else {
                    System.out.println(super.Results[1]);
                }
            }

            @Override
            public void onFailedConnectionAttempt(Context context) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Ha ocurrido un error")
                        .setMessage("pito")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
            }
        };

        ws.execute(new String[]{"ID"},
                new String[]{id});

//        System.out.println("Mail " + Comands.getMAIL() +"\n"+"Nom " + Comands.getNOM()+ "\n" + "Pass "+ Comands.getPASS());
        return correct[0];
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
