package edu.cecyt9.ipn.movil_link2band;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    Button btnSave, btnDelete;
    EditText txtNom;

    private OnFragmentInteractionListener mListener;

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
        btnSave = view.findViewById(R.id.save);
        btnSave.setOnClickListener(this);
        btnDelete = view.findViewById(R.id.delete);
        btnDelete.setOnClickListener(this);
        txtNom = view.findViewById(R.id.nom);
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
        if (v.getId() == btnDelete.getId()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.alert, null);
            alert.setTitle("Eliminar cuenta")
                    .setView(view)
                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText txt = view.findViewById(R.id.msj);
                            txt.setHint("Escribe tu contraseña");
                            if (!txt.getText().toString().equals("")) {

                            } else {

                            }
                        }
                    })
                    .setNegativeButton("cancelar", null).show();
        } else if (v.getId() == btnSave.getId()) {
            long id = 0;
            try {
                DatabaseHelper DB = new DatabaseHelper(getActivity());
                id = DB.insertNote(txtNom.getText().toString());
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Prueba")
                    .setMessage(String.valueOf(id))
                    .setCancelable(true)
            .show();
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
