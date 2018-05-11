package edu.cecyt9.ipn.movil_link2band.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public Long alataUSR(String id, String nom, String pass) {
        SQLiteDatabase DB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        values.put(Utilidades.CAMPO_NOM, nom);
        values.put(Utilidades.CAMPO_PASS, pass);
        Long idResult = DB.insert(Utilidades.TABLE_NAME, Utilidades.CAMPO_ID, values);
        Comands.setID(id);
        Comands.setNOM(nom);
        Comands.setPASS(pass);
        Comands.setLOC("null");
        DB.close();
        System.out.println("usuario registrado con exito");
        return idResult;
    }

    public Boolean consulta(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        //Arreglo para campos que quieres que regresen
        String[] campos = {Utilidades.CAMPO_ID, Utilidades.CAMPO_LOC, Utilidades.CAMPO_NOM, Utilidades.CAMPO_PASS};

        try {
            Cursor cursor = DB.query(Utilidades.TABLE_NAME, campos, Utilidades.CAMPO_ID + "=?", parameters, null, null,null);
            cursor.moveToFirst();
            Comands.setID(cursor.getString(0));
            Comands.setLOC(cursor.getString(1));
            Comands.setNOM(cursor.getString(2));
            Comands.setPASS(cursor.getString(3));
            cursor.close();
            return true;
        } catch (Exception e) {
            System.out.println("No existe usr");
            return false;
        }
    }

    public void actualiza(String id, String loc){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        values.put(Utilidades.CAMPO_LOC, loc);
        DB.update(Utilidades.TABLE_NAME, values, Utilidades.CAMPO_ID + "=?", parameters);
        DB.close();
        Comands.setID(id);
        Comands.setLOC(loc);
    }

    public void bajaUSR(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        DB.delete(Utilidades.TABLE_NAME, Utilidades.CAMPO_ID + "=?", parameters);
        System.out.println("Usr Eliminado");
        DB.close();
    }

    public int cuantos(){
        String countQuery = "SELECT * FROM " + Utilidades.TABLE_NAME;
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
