package edu.cecyt9.ipn.movil_link2band;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Alumno on 18/04/2018.
 */

public class WS_Cliente extends AsyncTask<String, String, String> {
    static final String NAMESPACE = "http://WebServer/";
    static final String URL = "http://20.145.192.168:8080/Server_L2B/WebServer?WSDL";
    static String METHODNAME = "";
    static String SOAP_ACTION = "";
    public boolean Failed = false, Ready = false;
    String[] Results;
    Context context;

    public WS_Cliente(String METHODNAME, Context context) {
        this.METHODNAME = METHODNAME;
        this.context = context;
        SOAP_ACTION = NAMESPACE + METHODNAME;
    }

    private ProgressDialog mProgress;
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        mProgress = new ProgressDialog(context);
        mProgress.setMessage("Estableciendo conexión...");
        mProgress.show();
    }

    @Override
    protected String doInBackground(String... params) {
        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("UserName", params[0]);
        request.addProperty("Pass", params[1]);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            int count = result.getPropertyCount();
            Results = new String[count];
            for (int i = 0; i < count; i++)
            {
                Results[i] = result.getPropertyAsString(i);
            }
            Ready = true;
        } catch (Exception e) {
            Failed = true;
            System.out.println("Valió verdura :'v " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        mProgress.dismiss();
        if (Failed) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Ha ocurrido un error")
                    .setMessage("No ha sido posible conectarse\nRevisa tu conexión a internet")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
        }
    }
}
