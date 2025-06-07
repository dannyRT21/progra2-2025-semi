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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IngresosLista extends Activity {
    Bundle parametros = new Bundle();
    FloatingActionButton fab;
    DB db;
    Cursor cIngresos;


    ListView ltsIngresos;
    final ArrayList<Ingresos> alIngresos = new ArrayList<>();
    final ArrayList<Ingresos> alIngresosCopia = new ArrayList<>();

    JSONArray jsonArrayIngresos;
    JSONObject jsonObjectIngresos;
    int posicion = 0;

    Ingresos misIngresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos_lista);

        parametros.putString("accion", "nuevo");
        db = new DB(this);
        obtenerDatosIngresos();

        fab = findViewById(R.id.fabRegresarIngreso);
        fab.setOnClickListener(view -> abrirVentana());
        buscarIngresos();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(jsonArrayIngresos.getJSONObject(posicion).getString("Concepto"));
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxNuevo) {
                abrirVentana();
            } else if (item.getItemId() == R.id.mnxModificar) {
                parametros.putString("accion", "modificar");
                JSONObject ingreso = jsonArrayIngresos.getJSONObject(posicion);
                parametros.putString("ingresos", ingreso.toString());
                parametros.putString("FuentedeIngreso", ingreso.getString("Categoria"));
                parametros.putString("fechaIngreso", ingreso.getString("Fecha"));
                parametros.putString("descripcionIngreso", ingreso.getString("Concepto"));
                parametros.putString("montoIngreso", ingreso.getString("Total"));
                abrirVentana();
            } else if (item.getItemId() == R.id.mnxEliminar) {
                eliminarIngreso();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private void eliminarIngreso() {
        try {
            String IdIngreso = jsonArrayIngresos.getJSONObject(posicion).getString("IdIngreso");
            String Concepto = jsonArrayIngresos.getJSONObject(posicion).getString("Concepto");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("¿Está seguro de eliminar a: ");
            confirmacion.setMessage(IdIngreso + " " + Concepto);
            confirmacion.setPositiveButton("Si", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_ingresos("eliminar", new String[]{IdIngreso});
                    if (respuesta.equals("ok")) {
                        obtenerDatosIngresos();
                        mostrarMsg("Registro eliminado");
                    } else {
                        mostrarMsg("Error: " + respuesta);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            confirmacion.create().show();
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void abrirVentana() {
        Intent intent = new Intent(this, agregar_ingresos.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void obtenerDatosIngresos() {
        try {
            cIngresos = db.lista_ingresos();
            jsonArrayIngresos = new JSONArray();
            if (cIngresos.moveToFirst()) {
                do {
                    Ingresos ingreso = new Ingresos(
                            cIngresos.getString(0), // IdIngreso
                            cIngresos.getString(1), // IdUsuario
                            cIngresos.getString(2), // Categoria
                            cIngresos.getString(3), // Fecha
                            cIngresos.getString(4), // Concepto
                            cIngresos.getString(5)  // Total
                    );
                    alIngresos.add(ingreso);

                    jsonObjectIngresos = new JSONObject();
                    jsonObjectIngresos.put("IdIngreso", ingreso.getIdIngreso());
                    jsonObjectIngresos.put("IdUsuario", ingreso.getIdUsuario());
                    jsonObjectIngresos.put("Categoria", ingreso.getFuenteIngreso());
                    jsonObjectIngresos.put("Fecha", ingreso.getFechaIngreso());
                    jsonObjectIngresos.put("Concepto", ingreso.getDescripcionIngreso());
                    jsonObjectIngresos.put("Total", ingreso.getMontoIngreso());

                    jsonArrayIngresos.put(jsonObjectIngresos);
                } while (cIngresos.moveToNext());
                mostrarDatosIngresos();
            } else {
                mostrarMsg("No hay ingresos que mostrar");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void mostrarDatosIngresos() {
        try {
            if (jsonArrayIngresos.length() > 0) {
                ltsIngresos = findViewById(R.id.ltsIngresos);
                alIngresos.clear();
                alIngresosCopia.clear();

                for (int i = 0; i < jsonArrayIngresos.length(); i++) {
                    JSONObject obj = jsonArrayIngresos.getJSONObject(i);
                    Ingresos ingreso = new Ingresos(
                            obj.getString("IdIngreso"),
                            obj.getString("IdUsuario"),
                            obj.getString("Categoria"),
                            obj.getString("Fecha"),
                            obj.getString("Concepto"),
                            obj.getString("Total")
                    );
                    alIngresos.add(ingreso);
                    alIngresosCopia.add(ingreso);
                }
                ltsIngresos.setAdapter(new IngresosAdaptador(this, alIngresos));
                registerForContextMenu(ltsIngresos);
            } else {
                mostrarMsg("No hay ingresos que mostrar");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void buscarIngresos() {
        TextView tempVal = findViewById(R.id.txtBuscarIngreso);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alIngresos.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if (buscar.length() <= 0) {
                    alIngresos.addAll(alIngresosCopia);
                } else {
                    for (Ingresos item : alIngresosCopia) {
                        if (item.getFuenteIngreso().toLowerCase().contains(buscar) ||
                                item.getFechaIngreso().toLowerCase().contains(buscar) ||
                                item.getMontoIngreso().toLowerCase().contains(buscar) ||
                                item.getDescripcionIngreso().toLowerCase().contains(buscar)) {
                            alIngresos.add(item);
                        }
                    }
                }
                ltsIngresos.setAdapter(new IngresosAdaptador(getApplicationContext(), alIngresos));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}