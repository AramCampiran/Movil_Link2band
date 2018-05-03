package edu.cecyt9.ipn.movil_link2band;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.security.Principal;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Database.Utilidades;

public class principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GeneralConfig.OnFragmentInteractionListener,
        SecurityMechanism.OnFragmentInteractionListener {

    String nom;
    TextView usuario;
    String id;
    DatabaseHelper conect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        Intent intent = getIntent();
        nom = intent.getStringExtra("nom");
        usuario = view.findViewById(R.id.nomUsr);
        usuario.setText("Hola " + nom);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new SecurityMechanism();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, fragment).commit();

        id = intent.getStringExtra("id");
        conect = new DatabaseHelper(this);
        if (!conect.consulta(id)) {
            Long idReturn = conect.alataUSR(id);
        }
        System.out.println(Comands.getID());
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
        if (id == R.id.action_settings) {
            conect = new DatabaseHelper(this);
            conect.bajaUSR(Comands.getID());
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Boolean fragmentTransaction = false;
        if (id == R.id.nav_mechanism) {
            // Handle the camera action
            fragment = new SecurityMechanism();
            fragmentTransaction = true;
        } else if (id == R.id.nav_settings) {
            fragment = new GeneralConfig();
            fragmentTransaction = true;
        } else if (id == R.id.logOut) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Cerrar sesion")
                    .setMessage("¿Esta seguro de cerrar sesión?")
                    .setNegativeButton("no", null)
                    .setPositiveButton("si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            conect = new DatabaseHelper(getApplicationContext());
                            conect.bajaUSR(Comands.getID());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).show();
        }

        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
