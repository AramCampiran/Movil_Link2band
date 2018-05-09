package edu.cecyt9.ipn.movil_link2band.Database;

/**
 * Created by Usuario on 02/05/2018.
 */

public class    Utilidades {

    public static final String CAMPO_ID = "ID";
    public static final String CAMPO_LOC = "LOC";
    public static final String CAMPO_NOM = "NOM";
    public static final String CAMPO_PASS = "PASS";
    public static final String TABLE_NAME = "usuarios";
    public static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME +"("+ CAMPO_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                              + CAMPO_NOM +" TEXT NOT NULL, "
                                                                              + CAMPO_PASS +" TEXT NOT NULL, "
                                                                              + CAMPO_LOC +" TEXT)";

}
