package edu.cecyt9.ipn.movil_link2band;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.Validacion;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user, pass;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.Log_userInput);
        pass = findViewById(R.id.Log_passInput);


        DatabaseHelper DB = new DatabaseHelper(this);
        if (!DB.selectIDs().equals("0")) {
            DB.consulta(Comands.getID());
            Intent intent = new Intent(this, principal.class);
            intent.putExtra("nom", Comands.getNOM());
            intent.putExtra("pass", Comands.getPASS());
            intent.putExtra("mail", Comands.getMAIL());
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Log_signBtn) {
            Validacion val = new Validacion();
            if (val.LogIn_Val(user, pass)) {
                @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.LogInMethod), this) {
                    @Override
                    public void onSuccessfulConnectionAttempt(Context context) {
                        if (Boolean.parseBoolean(super.Results[0])) {
                            Intent intent = new Intent(context, principal.class);
                            intent.putExtra("id", super.Results[1]);
                            intent.putExtra("nom", super.Results[2]);
                            intent.putExtra("pass", pass.getText().toString());
                            intent.putExtra("mail", super.Results[3]);
                            startActivity(intent);
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Problema para iniciar sesión")
                                    .setMessage(super.Results[1])
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            user.requestFocus();
                                        }
                                    }).show();
                        }
                    }
                };
                ws.execute(new String[]{getString(R.string.UserName), getString(R.string.Pass)}, new String[]{user.getText().toString(), pass.getText().toString()});
            } else {
                val.MarkTheError();
            }
        } else if (view.getId() == R.id.btn_Temp) {
            Intent intent = new Intent(this, principal.class);
            intent.putExtra("nom", "Juanito");
            intent.putExtra("pass", "123");
            startActivity(intent);
        } else if (view.getId() == R.id.Log_registroTxtV) {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.Log_recoveryTxtV) {
            final EditText mailInput = new EditText(this);
            mailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            mailInput.setHint(getString(R.string.prompt_userandmail));
            mailInput.requestFocus();
            final TextInputLayout Layout = new TextInputLayout(this);
            Layout.addView(mailInput);
            Layout.setPadding(20, 10, 20, 0);

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Recuperación de contraseña")
                    .setMessage("Completa el campo para restablecer tu contraseña")
                    .setView(Layout)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            Validacion val = new Validacion();
                            if (!val.Mail_val(mailInput)) {
                                val.ToastTheError(MainActivity.this);
                            } else {
                                WS_Cliente ws = new WS_Cliente(getString(R.string.RecuperacionMethod), MainActivity.this) {
                                    @Override
                                    public void onSuccessfulConnectionAttempt(Context context) {
                                        if (Boolean.parseBoolean(super.Results[0])) {
                                            dialogInterface.cancel();
                                        } else {
                                            mailInput.requestFocus();
                                        }
                                        Toast.makeText(context, super.Results[1], Toast.LENGTH_LONG).show();
                                    }
                                };
                                ws.execute(new String[]{getString(R.string.Mail)},
                                        new String[]{mailInput.getText().toString()});
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        }
    }
}