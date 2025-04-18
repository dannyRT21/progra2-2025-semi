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

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
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
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto);

        parametros.putString("accion", "nuevo");
        db = new DB(this);
        //ltsProductos = findViewById(R.id.ltsProductos); // Inicializar ListView

        fab = findViewById(R.id.fabRegresarProducto);
        fab.setOnClickListener(view -> AbrirVentana());
        listarDatos();
        buscarProducto();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getJSONObject("value").getString("codigo"));
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            if( item.getItemId()==R.id.mnxNuevo){
                AbrirVentana();
            }else if( item.getItemId()==R.id.mnxModificar){
                parametros.putString("accion", "modificar");
                parametros.putString("Producto", jsonArray.getJSONObject(posicion).getJSONObject("value").toString());
                AbrirVentana();
            } else if (item.getItemId()==R.id.mnxEliminar) {
                eliminarProducto();
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private void eliminarProducto() {
        try {
            String nombre = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("nombre");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("¿Está seguro de eliminar el producto?");
            confirmacion.setMessage(nombre);
            confirmacion.setPositiveButton("Sí", (dialog, which) -> {
                try {
                    di = new detectarInternet(this);
                    if (di.hayConexionInternet()) { // Online
                        JSONObject datosProducto = new JSONObject();
                        String _id = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("_id");
                        String _rev = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("_rev");
                        String url = utilidades.url_mto + "/" + _id + "?rev=" + _rev;
                        enviarDatosServidor objEnviarDatosServidor = new enviarDatosServidor(this);
                        String respuesta = objEnviarDatosServidor.execute(datosProducto.toString(), "DELETE", url).get();
                        JSONObject respuestaJSON = new JSONObject(respuesta);
                        if (!respuestaJSON.getBoolean("ok")) {
                            mostrarMsg("Producto eliminado con éxito.");
                        } else {
                            mostrarMsg("Error: " + respuesta);
                        }
                    }

                    String respuesta = db.administrar_productos("eliminar", new String[]{jsonArray.getJSONObject(posicion).getJSONObject("value").getString("idProducto")});
                    if (respuesta.equals("ok")) {
                        listarDatos();
                        mostrarMsg("Producto eliminado con éxito.");
                    } else {
                        mostrarMsg("Error: " + respuesta);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            confirmacion.create().show();
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void AbrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void listarDatos() {
        try {
            di= new detectarInternet(this);
            if (di.hayConexionInternet()) { // Online
                datosServidor = new obtenerDatosServidor();
                String respuesta = datosServidor.execute().get();
                 jsonObject = new JSONObject(respuesta);
                jsonArray = jsonObject.getJSONArray("rows");
                mostrarDatosProductos();
            } else { // Offline
              ObtenerDatoProductos();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
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
                ltsProductos = findViewById(R.id.ltsProductos);
                aProductos.clear();
                aProductosCopia.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i).getJSONObject("value");
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
                if (buscar.length() <= 0) {
                    aProductos.addAll(aProductosCopia);
                } else {
                    for (productos item : aProductosCopia) {
                        if (item.getNombre().toLowerCase().contains(buscar) ||
                                item.getIdProducto().toLowerCase().contains(buscar) ||
                                item.getCodigo().toLowerCase().contains(buscar)) {
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