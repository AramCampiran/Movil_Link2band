package edu.cecyt9.ipn.movil_link2band.Database;

/**
 * Created by Usuario on 29/04/2018.
 */

public class Comands {

    public static final String TABLE_NAME = "persona";

    public static final String COLUMN_ID = "id";

    private int id;
    private String note;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ")";

    public Comands(){}

    public Comands(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}