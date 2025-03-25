package com.ugb.miprimeraaplicacion;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class utilidades {

    static String url_consulta = "https://192.168.124.92/agenda/_design/agenda/_view/agenda";
    String url_nto = "https://192.168.124.92/agenda";

    static String  user = "admin";

    static String passed = "12345";

    static String credenciatesCodificuoss = Base64.getEncoder().encodeToString((user + ":" +passed).getBytes());
    public String generarunicold(){
        return java.util.UUID.randomUUID().toString();

    }
}
