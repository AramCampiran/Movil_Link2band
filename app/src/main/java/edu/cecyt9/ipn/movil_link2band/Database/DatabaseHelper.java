package edu.cecyt9.ipn.movil_link2band.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.zip.CheckedOutputStream;

/**
 * Created by Usuario on 29/04/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public DatabaseHelper(Context context) {
        super(context, Utilidades.TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(Utilidades.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int versionAntigua, int versionNueva) {
        // Drop older table if existed
        DB.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLE_NAME);
        // Create tables again
        onCreate(DB);
    }

    public Long alataUSR(String id) {
        SQLiteDatabase DB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        Long idResult = DB.insert(Utilidades.TABLE_NAME, Utilidades.CAMPO_ID, values);
        DB.close();
        System.out.println("usuario registrado con exito");
        return idResult;
    }

    public Boolean consulta(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        //Arreglo para campos que quieres que regresen
        String[] campos = {Utilidades.CAMPO_ID};

        try {
            Cursor cursor = DB.query(Utilidades.TABLE_NAME, campos, Utilidades.CAMPO_ID + "=?", parameters, null, null,null);
            cursor.moveToFirst();
            Comands comands = new Comands(cursor.getString(0));
            cursor.close();
            return true;
        } catch (Exception e) {
            System.out.println("No existe usr");
            return false;
        }
    }

    public void actualiza(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        DB.update(Utilidades.TABLE_NAME, values, Utilidades.CAMPO_ID + "=?", parameters);
        System.out.println("Registro cambiado");
    }

    public void bajaUSR(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        DB.delete(Utilidades.TABLE_NAME, Utilidades.CAMPO_ID + "=?", parameters);
        System.out.println("Usr Eliminado");
        DB.close();
    }

}
