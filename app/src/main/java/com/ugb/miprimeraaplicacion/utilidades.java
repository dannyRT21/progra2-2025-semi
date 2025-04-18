package com.ugb.miprimeraaplicacion;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;


public class utilidades {
    static String url_consulta = "http://192.168.202.92:5984/mandarino/_design/mandarino/_view/mandarino";
    static String url_mto = "http://192.168.202.92:5984/mandarino";
    static String user = "admin";
    static String passwd = "12345";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes());
    public String generarUnicoId(){
        return java.util.UUID.randomUUID().toString();
    }
}