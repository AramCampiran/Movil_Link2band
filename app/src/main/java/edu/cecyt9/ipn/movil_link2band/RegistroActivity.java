package edu.cecyt9.ipn.movil_link2band;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{
    EditText UserName, Email, Pass, RepeatPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setTitle(R.string.title_activity_registro);
        UserName = findViewById(R.id.R_et_username);
        Email = findViewById(R.id.R_et_mail);
        Pass = findViewById(R.id.R_et_password);
        RepeatPass = findViewById(R.id.R_et_repeatPassword);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ready) {
            Toast.makeText(this, "Registrado c:", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, principal.class);
            intent.putExtra("nom", UserName.getText().toString());
            startActivity(intent);
        } else if (view.getId() == R.id.btn_cancel) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
