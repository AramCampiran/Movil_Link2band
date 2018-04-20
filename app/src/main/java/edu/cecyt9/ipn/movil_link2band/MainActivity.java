package edu.cecyt9.ipn.movil_link2band;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nom, pass;

    private WS_Cliente ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nom = findViewById(R.id.et_User);
        pass = findViewById(R.id.et_Pass);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_Enter) {
            ws = new WS_Cliente("LogIn", this);
            ws.execute(nom.getText().toString().trim(), pass.getText().toString().trim());
            boolean Ready = false;
            while (!ws.Failed && !Ready) {
                Ready = ws.Ready;
                if (Ready) {
                    if (Boolean.parseBoolean(ws.Results[0])) {
                        Intent intent = new Intent(this, principal.class);
                        intent.putExtra("nom", nom.getText().toString());
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle("Datos incorrectos")
                                .setMessage(ws.Results[1])
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        nom.requestFocus();
                                    }
                                }).show();
                    }
                }
            }
        } else if (view.getId() == R.id.btn_temp) {
            Intent intent = new Intent(this, principal.class);
            intent.putExtra("nom", "Juanito");
            startActivity(intent);
        } else if (view.getId() == R.id.tv_registro) {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        }
    }
}
