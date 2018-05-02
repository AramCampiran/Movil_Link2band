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
    public Long insertNote(String note) {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Comands.COLUMN_NOTE, note);
            // insert row
            Long id = db.insert(Comands.TABLE_NAME, null, values) - 1;
            // close db connection
            db.close();
            return id;
        // return newly inserted row id
    }
    //Consultas
    public Comands getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Comands.TABLE_NAME,
                new String[]{Comands.COLUMN_ID, Comands.COLUMN_NOTE},
                Comands.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Comands object
        Comands comands = new Comands(
                cursor.getInt(cursor.getColumnIndex(Comands.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Comands.COLUMN_NOTE)));

        // close the db connection
        cursor.close();

        return comands;
    }



    public int getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Comands.TABLE_NAME, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    //Cambios
    public int updateNote(Comands note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Comands.COLUMN_NOTE, note.getNote());

        // updating row
        return db.update(Comands.TABLE_NAME, values, Comands.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    //Bajas
    public void deleteNote(Comands note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Comands.TABLE_NAME, Comands.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

}
