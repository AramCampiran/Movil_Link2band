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

    public  Comands(){}

    public Comands(String ID, String LOC, String NOM, String PASS, String mail,
                   String sMode, String block, String parBlock, String totBlock,
                   String duration, String tone, String msj) {
        Comands.ID = ID;
        Comands.LOC = LOC;
        Comands.NOM = NOM;
        Comands.PASS = PASS;
        Comands.MAIL = mail;
        Comands.SMODE = sMode;
        Comands.BLOCK = block;
        Comands.PARBLOCK = parBlock;
        Comands.TOTBLOCK = totBlock;
        Comands.DURATION = duration;
        Comands.TONE = tone;
        Comands.MSJ = msj;
    }





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
}