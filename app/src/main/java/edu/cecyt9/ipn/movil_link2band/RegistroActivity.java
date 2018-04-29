package edu.cecyt9.ipn.movil_link2band;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cecyt9.ipn.movil_link2band.Extras.Validacion;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText[] Campos;
    private final int[] Names = {R.string.UserName, R.string.Mail, R.string.Pass};
    private WS_Cliente ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Campos = new EditText[]{findViewById(R.id.Reg_userInput), findViewById(R.id.Reg_passInput),
                findViewById(R.id.Reg_repeatpassInput), findViewById(R.id.Reg_mailInput)};
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ready) {
            new AuxForWS();
        }
    }

//    private boolean ValidaTodo() {
//        for (int i = 0; i < 3; i++) {
//            if (!val.validaRegistro(Campos[i].getText().toString(), Names[i])) {
//                return false;
//            }
//        }
//        return true;
//    }

    public class AuxForWS extends WS_Cliente {

        public AuxForWS() {
            super(getString(R.string.RegistroMethod), RegistroActivity.this);
            super.execute(getString(R.string.UserName), Campos[0].getText().toString().trim(),
                    getString(R.string.Pass), Campos[1].getText().toString().trim(),
                    getString(R.string.Model), "Sansun",
                    getString(R.string.Mail), Campos[3].getText().toString().trim());
        }

        @Override
        public void OnSuccessfulConnectionAttempt(Context context) {
            if (Boolean.parseBoolean(super.Results[0])) {
                Intent intent = new Intent(context, principal.class);
                intent.putExtra("nom", Campos[0].getText().toString());
                intent.putExtra("id", super.Results[2]);
                startActivity(intent);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Problema en el registro")
                        .setMessage(super.Results[1])
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Campos[0].requestFocus();
                            }
                        }).show();
            }
        }
    }
}
