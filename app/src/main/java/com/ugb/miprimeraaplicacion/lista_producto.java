package com.ugb.miprimeraaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_producto extends Activity {

    Bundle parametros = new Bundle();
    Cursor cProductos;
    ListView ltsProductos;
    DB db;
    final ArrayList<productos> aProductos = new ArrayList<>();
    final ArrayList<productos> aProductosCopia = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    productos misProductos;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto);

        db = new DB(this);
        ltsProductos = findViewById(R.id.ltsProductos); // Inicializar ListView
        fab = findViewById(R.id.fabRegresarProducto);
        fab.setOnClickListener(view -> AbrirVentana());

        ObtenerDatoProductos(); // Llamar a la función para obtener datos
        buscarProducto();
    }

    private void AbrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void ObtenerDatoProductos() {
        try {
            cProductos = db.lista_productos();
            if (cProductos.moveToFirst()) {
                jsonArray = new JSONArray();
                do {
                    jsonObject = new JSONObject();
                    jsonObject.put("idProducto", cProductos.getString(0));
                    jsonObject.put("codigo", cProductos.getString(1));
                    jsonObject.put("nombre", cProductos.getString(2));
                    jsonObject.put("presentacion", cProductos.getString(3));
                    jsonObject.put("marca", cProductos.getString(4));
                    jsonObject.put("precio", cProductos.getString(5));
                    jsonObject.put("urlFoto", cProductos.getString(6));
                    jsonArray.put(jsonObject);
                } while (cProductos.moveToNext()); // Mover al siguiente registro
                mostrarDatosProductos();
            } else {
                mostrarMsg("No hay productos registrados");
                AbrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarDatosProductos() {
        try {
            if (jsonArray.length() > 0) {
                aProductos.clear();
                aProductosCopia.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    misProductos = new productos(
                            jsonObject.getString("idProducto"),
                            jsonObject.getString("codigo"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("presentacion"),
                            jsonObject.getString("marca"),
                            jsonObject.getString("precio"),
                            jsonObject.getString("urlFoto")
                    );
                    aProductos.add(misProductos);
                }
                aProductosCopia.addAll(aProductos);
                ltsProductos.setAdapter(new AdaptadorProductos(this, aProductos));
                registerForContextMenu(ltsProductos);
            } else {
                mostrarMsg("No hay productos registrados");
                AbrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            AbrirVentana();
        }
    }

    private void buscarProducto(){
        TextView tempVal = findViewById(R.id.txtBuscarProducto);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aProductos.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    aProductos.addAll(aProductosCopia);
                }else{
                    for (productos item: aProductosCopia){
                        if(item.getNombre().toLowerCase().contains(buscar) ||
                                item.getIdProducto().toLowerCase().contains(buscar) ||
                                item.getCodigo().toLowerCase().contains(buscar)){
                            aProductos.add(item);
                        }
                    }
                    ltsProductos.setAdapter(new AdaptadorProductos(getApplicationContext(), aProductos));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}