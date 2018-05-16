package edu.cecyt9.ipn.movil_link2band;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jaredrummler.android.device.DeviceName;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Extras.Validacion;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText user, mail, pass, repeatpass;
    private WS_Cliente ws;

    Validacion val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        user = findViewById(R.id.Reg_userInput);
        mail = findViewById(R.id.Reg_mailInput);
        pass = findViewById(R.id.Reg_passInput);
        repeatpass = findViewById(R.id.Reg_repeatpassInput);
        val = new Validacion();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ready) {
            if (val.Registro_Val(user, mail, pass, repeatpass)) {
                @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.RegistroMethod), this) {
                    @Override
                    public void onSuccessfulConnectionAttempt(Context context) {
                        if (Boolean.parseBoolean(super.Results[0])) {
                            Intent intent = new Intent(context, principal.class);
                            intent.putExtra("nom", user.getText().toString());
                            intent.putExtra("pass", pass.getText().toString());
                            intent.putExtra("id", super.Results[2]);
                            startActivity(intent);
                            consulta(super.Results[2]);
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Datos inválidos")
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
                ws.execute(new String[]{getString(R.string.UserName), getString(R.string.Pass), getString(R.string.Model), getString(R.string.Mail)},
                        new String[]{user.getText().toString(), pass.getText().toString(), new String(Build.MANUFACTURER + " " + DeviceName.getDeviceName()), mail.getText().toString()});
            } else {
                val.MarkTheError();
            }
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public boolean consulta(String id){
        @SuppressLint("StaticFieldLeak") WS_Cliente ws = new WS_Cliente(getString(R.string.ConsultaMethod), this){
            @Override
            public void onSuccessfulConnectionAttempt(Context context) {
                if (Boolean.parseBoolean(super.Results[0])){
                    Comands.setNOM(super.Results[1]);
                    Comands.setPASS(super.Results[2]);
                    Comands.setMAIL(super.Results[4]);
                }else{
                    System.out.println(super.Results[1]);
                }

            }
        };
        ws.execute(new String[]{"ID"},
                new String[]{id});
        return false;
    }

}
