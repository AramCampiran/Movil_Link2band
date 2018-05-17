package edu.cecyt9.ipn.movil_link2band;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.cecyt9.ipn.movil_link2band.Database.Comands;
import edu.cecyt9.ipn.movil_link2band.Database.DatabaseHelper;
import edu.cecyt9.ipn.movil_link2band.Extras.WS_Cliente;

public class principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GeneralConfig.OnFragmentInteractionListener,
        SecurityMechanism.OnFragmentInteractionListener,
        conectividad.OnFragmentInteractionListener{

    TextView usuario, BTout;
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        Intent intent = getIntent();
        nom = intent.getStringExtra("nom");
        usuario = view.findViewById(R.id.nomUsr);
        usuario.setText("Hola " + nom);

//        BTout = view.findViewById(R.id.BToutput);
        blutut();

        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new SecurityMechanism();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentPrincipal, fragment).commit();

        id = intent.getStringExtra("id");
        String pass = intent.getStringExtra("pass");
        String mail = intent.getStringExtra("mail");
        conect = new DatabaseHelper(this);
        if (conect.selectIDs().equals("0")) {
            Long idReturn = conect.alataUSR(id, nom, pass, mail);
            System.out.println("Nueva id " + Comands.getID());
        }else
            System.out.println("ID existente " + Comands.getID());


    }


    private void blutut() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

//        List<String> s = new ArrayList<String>();
        System.out.println("----------------------DISPOSITIVOS BLUTUT----------------------");
        for(BluetoothDevice bt : pairedDevices)
            System.out.println(bt.getName());
//            s.add(bt.getName());

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
        int id = item.getItemId();
        Fragment fragment = null;
        Boolean fragmentTransaction = false;
        if (id == R.id.nav_mechanism) {
            fragment = new SecurityMechanism();
            fragmentTransaction = true;
        } else if (id == R.id.nav_settings) {
            fragment = new GeneralConfig();
            fragmentTransaction = true;
        } else if (id == R.id.nav_blue){
            fragment = new conectividad();
            fragmentTransaction = true;
        } else if (id == R.id.logOut) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Cerrar sesion")
                    .setMessage("¿Esta seguro de cerrar sesión? \nSe perderán tus preferencias de los mecanismos de seguridad")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
