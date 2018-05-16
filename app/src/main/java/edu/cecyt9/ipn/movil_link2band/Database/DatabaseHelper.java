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

    public Long alataUSR(String id, String nom, String pass, String mail) {
        SQLiteDatabase DB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        values.put(Utilidades.CAMPO_NOM, nom);
        values.put(Utilidades.CAMPO_PASS, pass);
        values.put(Utilidades.CAMPO_MAIL, mail);
        Long idResult = DB.insert(Utilidades.TABLE_NAME, Utilidades.CAMPO_ID, values);
        Comands.setID(id);
        Comands.setNOM(nom);
        Comands.setPASS(pass);
        Comands.setMAIL(mail);
        Comands.setLOC("null");
        DB.close();
        System.out.println("usuario registrado con exito");
        return idResult;
    }

    public Boolean consulta(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        //Arreglo para campos que quieres que regresen
        String[] campos = {Utilidades.CAMPO_ID, Utilidades.CAMPO_LOC, Utilidades.CAMPO_NOM, Utilidades.CAMPO_PASS, Utilidades.CAMPO_MAIL,
                            Utilidades.CAMPO_SMODE, Utilidades.CAMPO_BLOCK, Utilidades.CAMPO_PARBLOCK, Utilidades.CAMPO_TOTBLOCK, Utilidades.CAMPO_DURATION,
                            Utilidades.CAMPO_TONE, Utilidades.CAMPO_MSJ};

        try {
            Cursor cursor = DB.query(Utilidades.TABLE_NAME, campos, Utilidades.CAMPO_ID + "=?", parameters, null, null,null);
            if (cursor.moveToFirst()) {
                Comands.setID(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_ID)));
                Comands.setLOC(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_LOC)));
                Comands.setNOM(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM)));
                Comands.setPASS(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_PASS)));
                Comands.setMAIL(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_MAIL)));

                Comands.setSMODE(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_SMODE)));
                Comands.setBLOCK(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_BLOCK)));
                Comands.setPARBLOCK(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_PARBLOCK)));
                Comands.setTOTBLOCK(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_TOTBLOCK)));
                Comands.setDURATION(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_DURATION)));
                Comands.setTONE(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_TONE)));
                Comands.setMSJ(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_MSJ)));
            }
            cursor.close();
            return true;
        } catch (Exception e) {
            System.out.println("No existe usr" + e);
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

    public void insertaMecanismos(String id, String sMode, String block, String parBlock,
                                  String totBlock,  String duration, String tone, String msj){
        SQLiteDatabase DB = getWritableDatabase();
        String [] parameters = {id};
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        values.put(Utilidades.CAMPO_SMODE, sMode);
        values.put(Utilidades.CAMPO_BLOCK, block);
        values.put(Utilidades.CAMPO_PARBLOCK, parBlock);
        values.put(Utilidades.CAMPO_TOTBLOCK, totBlock);
        values.put(Utilidades.CAMPO_DURATION, duration);
        values.put(Utilidades.CAMPO_TONE, tone);
        values.put(Utilidades.CAMPO_MSJ, msj);

        DB.update(Utilidades.TABLE_NAME, values, Utilidades.CAMPO_ID + "=?", parameters);
        DB.close();

        Comands.setSMODE(sMode);
        Comands.setBLOCK(block);
        Comands.setPARBLOCK(parBlock);
        Comands.setTOTBLOCK(totBlock);
        Comands.setDURATION(duration);
        Comands.setTONE(tone);
        Comands.setMSJ(msj);
    }

    public void bajaUSR(String id){
        SQLiteDatabase DB = getWritableDatabase();
        String[] parameters = {id};
        DB.delete(Utilidades.TABLE_NAME, Utilidades.CAMPO_ID + "=?", parameters);
        System.out.println("Usr Eliminado");
        DB.close();
        Comands.Clear();
    }

    public String selectIDs(){
        String respuesta = "0";
        String selectQuery = "SELECT * FROM " + Utilidades.TABLE_NAME;
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Comands.setID(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_ID)));
            respuesta = Comands.getID();
        }
        cursor.close();
        return respuesta;
    }

}
