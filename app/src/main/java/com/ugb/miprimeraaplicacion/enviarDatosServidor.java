package com.ugb.miprimeraaplicacion;

import static java.io.BufferedReader.*;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class enviarDatosServidor extends AsyncTask<String, String, String> {

 Context context;

 String respuesta = "";

 public enviarDatosServidor (Context context){
    this.context = context;
 }


    HttpURLConnection httpURLConnection;
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... parametros) {
        String jsonResponse = "";
        String jsonDatos = parametros[0];
        String metodo = parametros[1];
        String _url = parametros[2];

        BufferedReader bufferReader;

        try{
            URL url = new URL(_url);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(metodo);
            httpURLConnection.setRequestProperty("Authorization", "Basic " + utilidades.credenciatesCodificuoss);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");


            //enviar datos al server que no me da ni pija ayno

            Writer writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
            writer.write(jsonDatos);
            writer.close();

            //recibir respuesta del server q no me da ni pija ayno

            InputStream inputStream = httpURLConnection.getInputStream();
            if( inputStream==null ) return null;
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            respuesta = bufferReader.toString();
            //procesar la respuesta del servidor
            String linea;
            StringBuffer stringBuffer = new StringBuffer();
            while((linea = bufferReader.readLine()) != null){
                stringBuffer.append(linea);
            }
            if(stringBuffer.length()==0) return null;
            jsonResponse = stringBuffer.toString();
        }catch (Exception e){
            return e.getMessage();
        }
        finally {
            httpURLConnection.disconnect();
        }
        return jsonResponse;
    }
}