package edu.cecyt9.ipn.movil_link2band.Extras;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacion {

    private ArrayList<EditText> WrongData;
    private ArrayList<String> Error;
    private final int[] LengthBounds = {3, 50, 8};

    public boolean LogIn_Val(EditText... datos) {
        WrongData = new ArrayList<EditText>();
        Error = new ArrayList<String>();
        boolean isCorrect = true;
        for (int i = 0; i < datos.length; i++) {
            boolean tempCorrect = false;
            WrongData.add(datos[i]);
            if (datos[i].getText().toString().isEmpty()) {
                Error.add("Completa este campo");
            } else if (datos[i].length() < LengthBounds[0]) {
                Error.add("Introduce al menos " + LengthBounds[0] + " caracteres");
            } else if (datos[i].length() > LengthBounds[1]) {
                Error.add("No sobrepases los " + LengthBounds[1] + " caracteres");
            } else {
                tempCorrect = true;
                WrongData.remove(datos[i]);
            }
            if (!tempCorrect) isCorrect = false;
        }
        return isCorrect;
    }

    public boolean Registro_Val(EditText... datos) {
        WrongData = new ArrayList<EditText>();
        Error = new ArrayList<String>();
        Pattern patternMail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[A-Za-z]{2,})$");
        Pattern patternNom = Pattern.compile("^[\\w]+(\\.[\\w])*$");
        Matcher matcher0 = null, matcher1 = null;
        boolean isCorrect = true;
        for (int i = 0; i < datos.length; i++) {
            boolean tempCorrect = false;
            WrongData.add(datos[i]);
            if (i == 0) matcher0 = patternNom.matcher(datos[i].getText().toString());
            else if (i == 1) matcher1 = patternMail.matcher(datos[i].getText().toString());

            if (datos[i].getText().toString().isEmpty()) {
                Error.add("Completa este campo");
            } else if (datos[i].length() < LengthBounds[0]) {
                Error.add("Introduce al menos " + LengthBounds[0] + " caracteres");
            } else if (datos[i].length() > LengthBounds[1]) {
                Error.add("No sobrepases los " + LengthBounds[1] + " caracteres");
            } else if (i == 0 && !matcher0.find()) {
                Error.add("No introducir caracteres especiales o espacios en blanco");
            } else if (i == 1 && !matcher1.find()) {
                Error.add("Introduce un correo valido");
            } else if (i == 2 && !datos[i].getText().toString().equals(datos[3].getText().toString())) {
                Error.add("Las contraseñas no coinciden");
            } else {
                tempCorrect = true;
                WrongData.remove(datos[i]);
            }
            if (!tempCorrect) isCorrect = false;
        }
        return isCorrect;
    }


    public void MarkTheError() {
        for (int i = 0; i < WrongData.size(); i++) {
            WrongData.get(i).setError(Error.get(i));
        }
        WrongData.get(0).requestFocus();
    }

}
