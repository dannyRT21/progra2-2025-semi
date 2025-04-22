package com.ugb.miprimeraaplicacion;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;


public class utilidades {
    static String url_consulta = "http://192.168.17.92:5984/dani/_design/olimpia/_view/olimpia";
    static String url_mto = "http://192.168.17.92:5984/dani";
    static String user = "admin";
    static String passwd = "12345";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes());
    public String generarUnicoId(){
        return java.util.UUID.randomUUID().toString();
    }
}