package edu.cecyt9.ipn.movil_link2band;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import Bluetooth.conectividad;
import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.HiloBluetooth;

public class principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GeneralConfig.OnFragmentInteractionListener,
        SecurityMechanism.OnFragmentInteractionListener,
        conectividad.OnFragmentInteractionListener {

    TextView usuario;
    String nom, id;
    DatabaseHelper conect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        Intent intent = getIntent();
        nom = intent.getStringExtra("nom");
        usuario = view.findViewById(R.id.nomUsr);
        usuario.setText("Hola " + nom);


        navigationView.setNavigationItemSelectedListener(this);

        id = intent.getStringExtra("id");
        String pass = intent.getStringExtra("pass");
        String mail = intent.getStringExtra("mail");
        conect = new DatabaseHelper(this);
        if (conect.selectIDs().equals("0")) {
            Long idReturn = conect.alataUSR(id, nom, pass, mail);
            System.out.println("Nueva id " + Comands.getID());
        } else
            System.out.println("ID existente " + Comands.getID());

        Fragment fragment = new SecurityMechanism();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, fragment).commit();
        getSupportActionBar().setTitle("Mecanismos de seguridad");
        navigationView.setCheckedItem(R.id.nav_mechanism);



    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String title = "L2B";
        int id = item.getItemId();
        Fragment fragment = null;
        Boolean fragmentTransaction = false;
        if (id == R.id.nav_mechanism) {
            fragment = new SecurityMechanism();
            fragmentTransaction = true;
            title = "Mecanismos de seguridad";
        } else if (id == R.id.nav_settings) {
            fragment = new GeneralConfig();
            fragmentTransaction = true;
            title = "Configuración general";
        } else if (id == R.id.nav_blue) {
            fragment = new conectividad();
            fragmentTransaction = true;
            title = "Conectividad";
        } else if (id == R.id.logOut) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Cerrar sesión")
                    .setMessage("¿Esta seguro de cerrar sesión? \nSe perderán tus preferencias de los mecanismos de seguridad")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            conect = new DatabaseHelper(getApplicationContext());
                            conect.bajaUSR(Comands.getID());
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).show();
        }

        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
