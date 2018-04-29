package edu.cecyt9.ipn.movil_link2band.Extras;

import edu.cecyt9.ipn.movil_link2band.R;

public class validaciones_old {

    private String _msj;

    public String getMsj() {
        return _msj;
    }


    private boolean validaCorreo(String mail) {
        String[] mailSplited = mail.split("@");
        if (mailSplited.length == 2) {
            if (mailSplited[1].split("\\.").length >= 2) {
                return !tieneVacios(mail);
            } else {
                _msj = "Introduce un correo válido";
            }
        } else{
            _msj = "Introduce un correo válido";
        }
        return false;
    }

    private boolean tieneVacios(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            if (txt.charAt(i) == ' ') {
                _msj = "Introduce un correo válido";
                return true;
            }
        }
        return false;
    }

    private boolean validaLongitud(String txt) {
        if (txt.length() > 200) {
            _msj = txt + " es demasiado largo";
            return false;
        }
        return true;
    }

    public boolean validaRegistro(String parametro, int parameterName) {
        boolean esValido = (!parametro.isEmpty() && validaLongitud(parametro));
        if (parameterName == R.string.Mail) {
            esValido = (esValido && validaCorreo(parametro));
        }
        return esValido;
    }
}
