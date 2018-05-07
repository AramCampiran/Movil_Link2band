package edu.cecyt9.ipn.movil_link2band.Database;

/**
 * Created by Usuario on 29/04/2018.
 */

public class Comands {

    private static String ID;
    private static String LOC;

    public  Comands(){}

    public Comands(String ID, String LOC) {
        this.ID = ID;
        this.LOC = LOC;
    }

    public static String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public static String getLOC() {
        return LOC;
    }

    public void setLOC(String LOC) {
        this.LOC = LOC;
    }
}