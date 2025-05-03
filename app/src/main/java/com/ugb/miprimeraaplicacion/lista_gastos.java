package com.ugb.miprimeraaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_gastos extends Activity {

    FloatingActionButton fab;
    Bundle parametros = new Bundle();
    DB db;
    Cursor cGastos;

    ListView ltsGastos;
    final ArrayList<Gastos> alGastos = new ArrayList<Gastos>();
    final ArrayList<Gastos> alGastosCopia = new ArrayList<Gastos>();

    JSONArray jsonArray;
    JSONObject jsonObject;
    Gastos misGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        db = new DB(this);
        obtenerDatosGastos();

        fab = findViewById(R.id.fabRegresarGasto);
        fab.setOnClickListener(view -> {
           ;
        abrirVentana();});
    }

    private void abrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void obtenerDatosGastos() {
        try {
            cGastos = db.lista_gastos();
            if (cGastos.moveToFirst()) {
                jsonArray = new JSONArray();
                do {
                    misGastos = new Gastos(cGastos.getString(0), cGastos.getString(1), cGastos.getString(2), cGastos.getString(3), cGastos.getString(4), cGastos.getString(5));
                    alGastos.add(misGastos);
                    jsonObject = new JSONObject();
                    jsonObject.put("IdGasto", misGastos.getIdGasto());
                    jsonObject.put("IdUsuario", misGastos.getIdUsuario());
                    jsonObject.put("Categoria", misGastos.getCategoria());
                    jsonObject.put("Fecha", misGastos.getFecha());
                    jsonObject.put("Concepto", misGastos.getConcepto());
                    jsonObject.put("Total", misGastos.getTotal());
                    jsonArray.put(jsonObject);
                } while (cGastos.moveToNext());
                mostrarDatosGastos();

            } else {
                mostrarMsg("No hay gastos que mostrar, vete alv");
                abrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("eror: " + e.getMessage());
        }
    }
    private void mostrarDatosGastos() {
        try {
            if (jsonArray.length()>0){
                ltsGastos = findViewById(R.id.ltsGastos);
                alGastos.clear();
                alGastosCopia.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    misGastos = new Gastos(jsonObject.getString("IdGasto"),
                            jsonObject.getString("IdUsuario"),
                            jsonObject.getString("Categoria"),
                            jsonObject.getString("Fecha"),
                            jsonObject.getString("Concepto"),
                            jsonObject.getString("Total"));

                    alGastos.add(misGastos);
                    ltsGastos.setAdapter(new AdaptadorGastos(this, alGastos));
                    registerForContextMenu(ltsGastos);
                    }

            }else {
                mostrarMsg("No hay gastos que mostrar");
                abrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }

    }
        private void mostrarMsg (String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }


