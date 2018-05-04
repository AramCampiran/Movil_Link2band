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
public class GeneralConfig extends Fragment implements View.OnClickListener{
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
    ImageButton editNom, editMail, editPass, editPass2;
    TextView txtName, txtMail, txtPass, txtPass2;

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
        consulta();
        return view;
    }
    private void getValues() {
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente("GetResultado", getActivity()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                txtName.setText(super.Results[1]);
                txtMail.setText(super.Results[2]);
                String pass = super.Results[4];
                pass.replaceAll(pass, "•");
                txtPass.setText(pass);

            }
        };
        ws.execute(new String[]{"ID"},
                new String[]{Comands.getID()});
    }

    private void consulta() {
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.ConsultaMethod), getActivity()) {
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                if (Boolean.parseBoolean(super.Results[0])) {
                    Toast.makeText(getContext(), "Consulta bien", Toast.LENGTH_LONG).show();
                    getValues();
//                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                    alert.setTitle("Editar datos")
//                            .setMessage(super.Results[1])
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    startActivity(new Intent(getActivity(), MainActivity.class));
//                                }
//                            }).show();
                } else {
                    Toast.makeText(getContext(), "Consulta mal", Toast.LENGTH_LONG).show();
//                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                    alert.setTitle("Datos inválidos")
//                            .setMessage(super.Results[1])
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                }
//                            }).show();
                }
            }
        };
        ws.execute(new String[]{"ID"},
                new String[]{Comands.getID()});
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
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View vi = inflater.inflate(R.layout.alert, null);
            alert.setTitle("Verifica contraseña")
                    .setView(vi)
                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText txt = vi.findViewById(R.id.msj);
                            txt.setHint("contraseña actual");
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
            txtName.setText(alert("Nuevo nombre", "Nombre"));
        } else if (v.getId() == editMail.getId()) {
            alert("Nuevo correo", "Correo");
        } else if (v.getId() == editPass.getId()) {
            alert("Nueva contraseña", "Pass");
        }
    }

    private String alert(String hint, final String campo) {
        final String[] cambio = {""};
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.alert, null);
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
        deleteDialog.setView(deleteDialogView);
        deleteDialog.setTitle("Guardar cambios");
        deleteDialog.setMessage("Por favor escribe tu contraseña para verificar tu identidad");
        final EditText txt = deleteDialogView.findViewById(R.id.msj);
        txt.setHint("Contraseña actual");
        final EditText nuevo = deleteDialogView.findViewById(R.id.cambio);
        nuevo.setVisibility(View.VISIBLE);
        nuevo.setHint(hint);
        deleteDialog.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!nuevo.getText().toString().equals("") && !txt.getText().toString().equals("")) {
                    saveChanges(nuevo.getText().toString(), txt.getText().toString(), campo);
                    cambio[0] = nuevo.getText().toString();
                }
            }
        });
        deleteDialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nuevo.setText("");
                txt.setText("");
            }
        });
        deleteDialog.show();
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
                                    startActivity(new Intent(getActivity(), MainActivity.class));
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
