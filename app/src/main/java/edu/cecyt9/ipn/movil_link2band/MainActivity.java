package edu.cecyt9.ipn.movil_link2band;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.security.Principal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.cecyt9.ipn.movil_link2band.Extras.Validacion;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.Log_userInput);
        pass = findViewById(R.id.Log_passInput);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Log_signBtn) {
            Validacion val = new Validacion();
            if (val.LogIn_Val(user, pass)) {
                new AuxForWS();
            } else {
                val.MarkTheError();
            }
        } else if (view.getId() == R.id.btn_Temp) {
//            Intent intent = new Intent(this, LoginActivity.class);
            Intent intent = new Intent(this, principal.class);
            intent.putExtra("nom", "Juanito");
            startActivity(intent);
        } else if (view.getId() == R.id.Log_toregisterTextV) {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        }
    }

    public class AuxForWS extends WS_Cliente {

        public AuxForWS() {
            super(getString(R.string.LogInMethod), MainActivity.this);
            super.execute(getString(R.string.UserName), user.getText().toString().trim(),
                    getString(R.string.Pass), pass.getText().toString().trim());
        }

        @Override
        public void OnSuccessfulConnectionAttempt(Context context) {
            if (Boolean.parseBoolean(super.Results[0])) {
                Intent intent = new Intent(context, principal.class);
                intent.putExtra("nom", user.getText().toString());
                intent.putExtra("id", super.Results[1]);
                startActivity(intent);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Problema al iniciar sesión")
                        .setMessage(super.Results[1])
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.requestFocus();
                            }
                        }).show();
            }
        }
    }
}