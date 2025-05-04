package com.ugb.miprimeraaplicacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

    Bundle parametros = new Bundle();
    FloatingActionButton fab;
    DB db;
    Cursor cGastos;

    ListView ltsGastos;
    final ArrayList<Gastos> alGastos = new ArrayList<Gastos>();
    final ArrayList<Gastos> alGastosCopia = new ArrayList<Gastos>();

    JSONArray jsonArray;
    JSONObject jsonObject;
    Gastos misGastos;

    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        parametros.putString("accion", "nuevo");
        db = new DB(this);
        obtenerDatosGastos();

        fab = findViewById(R.id.fabRegresarGasto);
        fab.setOnClickListener(view -> abrirVentana());
        buscarGastos();

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            // Cambia "gasto" por "Concepto" o la clave correcta
            menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getString("Concepto"));
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxNuevo) {
                abrirVentana();

            } else if (item.getItemId() == R.id.mnxModificar) {
                parametros.putString("accion", "modificar");
                // Cambia "idGasto" por "IdGasto" para que coincida con las claves del JSON
                parametros.putString("gastos", jsonArray.getJSONObject(posicion).toString());
                abrirVentana();

            } else if (item.getItemId() == R.id.mnxEliminar) {
               eliminarGasto();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }


    private void eliminarGasto(){
        try{
            String IdGasto = jsonArray.getJSONObject(posicion).getString("IdGasto");
            String Categoria = jsonArray.getJSONObject(posicion).getString("Categoria");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("Esta seguro de eliminar a: ");
            confirmacion.setMessage(IdGasto + " " + Categoria);
            confirmacion.setPositiveButton("Si", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_gastos("eliminar", new String[]{jsonArray.getJSONObject(posicion).getString("IdGasto")});
                    if(respuesta.equals("ok")) {
                        obtenerDatosGastos();
                        mostrarMsg("Registro eliminado");
                        mostrarMsg("Error: " + respuesta);
                    }
                }catch (Exception e){
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            confirmacion.create().show();
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void abrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
      intent.putExtras(parametros);
        startActivity(intent);
    }

    private void obtenerDatosGastos() {
        try {
            cGastos = db.lista_gastos();
            if (cGastos.moveToFirst()) {
                jsonArray = new JSONArray();
                do {
                    misGastos = new Gastos(cGastos.getString(0), cGastos.getString(1), cGastos.getString(2), cGastos.getString(3), cGastos.getString(4), cGastos.getString(5), cGastos.getString(6));
                    alGastos.add(misGastos);
                    jsonObject = new JSONObject();
                    jsonObject.put("IdGasto", misGastos.getIdGasto());
                    jsonObject.put("IdUsuario", misGastos.getIdUsuario());
                    jsonObject.put("Categoria", misGastos.getCategoria());
                    jsonObject.put("Fecha", misGastos.getFecha());
                    jsonObject.put("Concepto", misGastos.getConcepto());
                    jsonObject.put("Total", misGastos.getTotal());
                    jsonObject.put("UrlFoto", misGastos.getUrlFoto());
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
                            jsonObject.getString("Total"),
                            jsonObject.getString("UrlFoto"));


                    alGastos.add(misGastos);
                    alGastosCopia.add(misGastos);
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
    private void buscarGastos(){
        TextView tempVal = findViewById(R.id.txtBuscarGasto);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alGastos.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    alGastos.addAll(alGastosCopia);
                }else{
                    for (Gastos item: alGastosCopia){
                        if(item.getCategoria().toLowerCase().contains(buscar) ||
                                item.getFecha().toLowerCase().contains(buscar) ||
                                item.getTotal().toLowerCase().contains(buscar)||
                                    item.getConcepto().toLowerCase().contains(buscar)){
                            alGastos.add(item);
                        }
                    }
                    ltsGastos.setAdapter(new AdaptadorGastos(getApplicationContext(), alGastos));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
        private void mostrarMsg (String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }


