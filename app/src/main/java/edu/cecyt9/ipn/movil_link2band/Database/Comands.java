package edu.cecyt9.ipn.movil_link2band.Database;

/**
 * Created by Usuario on 29/04/2018.
 */

public class Comands {

    private static String ID;
    private static String LOC;
    private static String NOM;
    private static String PASS;
    private static String MAIL;

    private static String SMODE;
    private static String BLOCK;
    private static String PARBLOCK;
    private static String TOTBLOCK;
    private static String DURATION;
    private static String TONE;
    private static String MSJ;
    private static String ADDRESS;

    public  Comands(){}

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {Comands.ID = ID;}

    public static String getLOC() {
        return LOC;
    }

    public static void setLOC(String LOC) {Comands.LOC = LOC;}

    public static String getNOM() {return NOM;}

    public static void setNOM(String NOM) {Comands.NOM = NOM;}

    public static String getPASS() {
        return PASS;
    }

    public static void setPASS(String PASS) {Comands.PASS = PASS;}

    public static String getMAIL() {
        return MAIL;
    }

    public static void setMAIL(String MAIL) {Comands.MAIL = MAIL;}

    public static void Clear(){
        Comands.ID = null;
        Comands.LOC = null;
        Comands.NOM = null;
        Comands.PASS = null;
        Comands.MAIL = null;
        Comands.SMODE = null;
        Comands.BLOCK = null;
        Comands.PARBLOCK = null;
        Comands.TOTBLOCK = null;
        Comands.DURATION = null;
        Comands.TONE = null;
        Comands.MSJ = null;
    }

    public static String getSMODE() {return SMODE;}

    public static void setSMODE(String SMODE) {Comands.SMODE = SMODE;}

    public static String getBLOCK() {return BLOCK;}

    public static void setBLOCK(String BLOCK) {Comands.BLOCK = BLOCK;}

    public static String getPARBLOCK() {return PARBLOCK;}

    public static void setPARBLOCK(String PARBLOCK) {Comands.PARBLOCK = PARBLOCK;}

    public static String getTOTBLOCK() {return TOTBLOCK;}

    public static void setTOTBLOCK(String TOTBLOCK) {Comands.TOTBLOCK = TOTBLOCK;}

    public static String getDURATION() {return DURATION;}

    public static void setDURATION(String DURATION) {Comands.DURATION = DURATION;}

    public static String getTONE() {return TONE;}

    public static void setTONE(String TONE) {Comands.TONE = TONE;}

    public static String getMSJ() {return MSJ;}

    public static void setMSJ(String MSJ) {Comands.MSJ = MSJ;}

    public static String getADDRESS() {
        return ADDRESS;
    }

    public static void setADDRESS(String ADDRESS) {
        Comands.ADDRESS = ADDRESS;
    }
}