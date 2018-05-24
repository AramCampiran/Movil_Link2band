package edu.cecyt9.ipn.movil_link2band.Database;

/**
 * Created by Usuario on 02/05/2018.
 */

public class Utilidades {

    public static final String CAMPO_ID = "ID";
    public static final String CAMPO_LOC = "LOC";
    public static final String CAMPO_NOM = "NOM";
    public static final String CAMPO_PASS = "PASS";
    public static final String CAMPO_MAIL = "MAIL";

    public static final String CAMPO_SMODE = "SMODE";
    public static final String CAMPO_BLOCK = "BLOK";
    public static final String CAMPO_PARBLOCK = "PARBLOCK";
    public static final String CAMPO_TOTBLOCK = "TOTBLOCK";
    public static final String CAMPO_DURATION = "DURATION";
    public static final String CAMPO_TONE = "TONE";
    public static final String CAMPO_URISTRING = "URISTRING";
    public static final String CAMPO_MSJ = "MSJ";

    public static final String TABLE_NAME = "usuarios";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_NOM + " TEXT NOT NULL, "
            + CAMPO_PASS + " TEXT NOT NULL, "
            + CAMPO_LOC + " TEXT, "
            + CAMPO_MAIL + " TEXT, "
            + CAMPO_SMODE + " TEXT, "
            + CAMPO_BLOCK + " TEXT, "
            + CAMPO_PARBLOCK + " TEXT, "
            + CAMPO_TOTBLOCK + " TEXT, "
            + CAMPO_DURATION + " TEXT, "
            + CAMPO_TONE + " TEXT, "
            + CAMPO_URISTRING + " TEXT, "
            + CAMPO_MSJ + " TEXT)";


}
