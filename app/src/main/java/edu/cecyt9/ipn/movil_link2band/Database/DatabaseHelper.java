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
    private static final String DATABASE_NAME = "bdL2B_lite";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Comands.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Comands.TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

        //Altas
    public Boolean insertID(String id) {
            // get writable database as we want to write data
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Comands.COLUMN_ID, id);
            // insert row
            db.insert(Comands.TABLE_NAME, null, values);
            // close db connection
            db.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }

        // return newly inserted row id
    }
    //Consultas
    public Comands getNote() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Comands.TABLE_NAME,
                new String[]{Comands.COLUMN_ID},
                Comands.COLUMN_ID,
                null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Comands object
        Comands comands = new Comands(
                cursor.getInt(cursor.getColumnIndex(Comands.COLUMN_ID)));
        // close the db connection
        cursor.close();

        return comands;
    }



    //Cambios
    public void update(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Comands.COLUMN_ID, id);

        // updating row
        db.update(Comands.TABLE_NAME, values, Comands.COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }

    //Bajas
    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Comands.TABLE_NAME, Comands.COLUMN_ID + " = ?",
                new String[]{Comands.COLUMN_ID});
        db.close();
    }

}
