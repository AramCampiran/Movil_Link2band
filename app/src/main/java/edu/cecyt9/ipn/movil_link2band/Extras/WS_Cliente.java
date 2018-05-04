package edu.cecyt9.ipn.movil_link2band.Extras;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import edu.cecyt9.ipn.movil_link2band.MainActivity;


@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class WS_Cliente extends AsyncTask<String[], String, Boolean> {

    private static final String NAMESPACE = "http://WebServer/";
    private static String URL = "http://192.168.0.4:8080/Server_L2B/WebServer?WSDL";
    private static String METHODNAME;
    private static String SOAP_ACTION;
    private Context context;
    private ProgressDialog PD;
    public String[] Results;

    public WS_Cliente(String METHODNAME, Context context) {
        this.METHODNAME = METHODNAME;
        this.context = context;
        SOAP_ACTION = NAMESPACE + METHODNAME;
    }

    @Override 
    protected void onPreExecute() {
        super.onPreExecute();
        PD = new ProgressDialog(context);
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setTitle("Espere un momento");
        PD.setMessage("Estableciendo conexión...");
        PD.setCancelable(true);
        PD.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(true);
            }
        });
        PD.setCanceledOnTouchOutside(false);
        PD.show();
    }

    @Override
    protected Boolean doInBackground(String[]... params) { //DEBEN ser 2 arreglos, uno de "nombres" y otro de valores
        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        for (int i = 0; i < params[0].length; i++) {
            request.addProperty(params[0][i], params[1][i]);
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            int count = result.getPropertyCount();
            Results = new String[count];
            for (int i = 0; i < count; i++) {
                Results[i] = result.getPropertyAsString(i);
            }
            return true;
        } catch (Exception e) {
            Log.d("Connection Error", e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        PD.dismiss();
        if (result) {
            onSuccessfulConnectionAttempt(context);
        } else {
            onFailedConnectionAttempt(context);
        }
    }

    private void onFailedConnectionAttempt(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Ha ocurrido un error")
                .setMessage("No ha sido posible conectarse\nRevisa tu conexión a internet")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    public void onSuccessfulConnectionAttempt(Context context) {}
}
