package com.ugb.miprimeraaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_producto extends Activity {

    Bundle parametros = new Bundle();

    Cursor cProductos;

    DB db;

    final ArrayList<productos> aProductos = new ArrayList<productos>();
    final ArrayList<productos> aProductosCopia = new ArrayList<productos>();

    JSONArray jsonArray;
    JSONObject jsonObject;

    productos misProductos;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DB(this);
        setContentView(R.layout.activity_lista_producto);
        fab = findViewById(R.id.fabRegresarProducto);
        fab.setOnClickListener(view -> {
            AbrirVentana();

        });
    }

    private void AbrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void ObtenerDatoProductos() {
        try {
    cProductos = db.lista_productos();
    if (cProductos.moveToFirst()){
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
            jsonArray.put(jsonObject);}while (cProductos.moveToFirst());
            mostrarDatosProductos();


    }else{
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
                ListView ltsProductos = findViewById(R.id.ltsProductos); // 💡 Cambio importante aquí
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
                aProductosCopia.addAll(aProductos); // 💡 Aquí estaba otro error, agregaba solo el último producto

                ltsProductos.setAdapter(new AdaptadorProductos(this, aProductos));
                registerForContextMenu(ltsProductos);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            AbrirVentana();
        }
    }

    private  void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
