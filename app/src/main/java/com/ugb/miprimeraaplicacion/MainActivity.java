package com.ugb.miprimeraaplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempVal;
    DB db;
    String accion = "nuevo", idProducto = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        btn = findViewById(R.id.btnGuardarproducto);
        btn.setOnClickListener(view->guardarProducto());

        fab = findViewById(R.id.fabListaProductos);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
    }
    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("Producto"));
                idProducto = datos.getString("idProducto");

                tempVal = findViewById(R.id.txtCodigo);
                tempVal.setText(datos.getString("codigo"));

                tempVal = findViewById(R.id.txtNombreProducto);
                tempVal.setText(datos.getString("nombre"));

                tempVal = findViewById(R.id.txtpresentacion);
                tempVal.setText(datos.getString("presentacion"));

                tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(datos.getString("marca"));

                tempVal = findViewById(R.id.txtPrecio);
                tempVal.setText(datos.getString("precio"));
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void abrirVentana(){
        Intent intent = new Intent(this, lista_producto.class);
        startActivity(intent);
    }
    private void guardarProducto() {
        tempVal = findViewById(R.id.txtCodigo);
        String nombre = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtNombreProducto);
        String direccion = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtpresentacion);
        String telefono = tempVal.getText().toString();
        tempVal = findViewById(R.id.txtMarca);
        String email = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtPrecio);
        String dui = tempVal.getText().toString();

        String[] datos = {"",  nombre, direccion, telefono, email, dui, ""};
        db.administrar_productos("agregar", datos);
        Toast.makeText(getApplicationContext(), "Registro guardado con exito.", Toast.LENGTH_LONG).show();
        abrirVentana();
    }
}