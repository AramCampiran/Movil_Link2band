package edu.cecyt9.ipn.movil_link2band.Database;

/**
 * Created by Usuario on 29/04/2018.
 */

public class Comands {

    private static String ID;
    private static String LOC;
    private static String NOM;
    private static String PASS;

    public  Comands(){}

    public Comands(String ID, String LOC, String NOM, String PASS) {
        Comands.ID = ID;
        Comands.LOC = LOC;
        Comands.NOM = NOM;
        Comands.PASS = PASS;
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {Comands.ID = ID;}

    public static String getLOC() {
        return LOC;
    }

    public static void setLOC(String LOC) {Comands.LOC = LOC;}

    public static String getNOM() {
        return NOM;
    }

    public static void setNOM(String NOM) {Comands.NOM = NOM;}

    public static String getPASS() {
        return PASS;
    }

    public static void setPASS(String PASS) {Comands.PASS = PASS;}

    public static void Clear(){
        Comands.ID = null;
        Comands.LOC = null;
        Comands.NOM = null;
        Comands.PASS = null;
    }
}