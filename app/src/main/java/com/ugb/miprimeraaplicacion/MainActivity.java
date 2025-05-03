package com.ugb.miprimeraaplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Button btn;
    Spinner spnCategoria;
    TextView tempVal;
    DB db;
    String accion = "nuevo", idGasto = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DB(this);
        btn = findViewById(R.id.btnguardarGasto);
        btn.setOnClickListener(View -> guardarAmigo());

        fab = findViewById(R.id.fabVerGastos);
        fab.setOnClickListener(view -> AbrirVentana());
        spnCategoria = findViewById(R.id.spncategoria);
        mostrarDatos();
    }

    private void mostrarDatos() {
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("gastos"));
                idGasto = datos.getString("IdGasto");

                tempVal = findViewById(R.id.txtfechaGasto);
                tempVal.setText(datos.getString("Fecha"));

                tempVal = findViewById(R.id.txtconceptoGasto);
                tempVal.setText(datos.getString("Concepto"));

                tempVal = findViewById(R.id.txtTotal);
                tempVal.setText(datos.getString("Total"));

                // Aquí puedes establecer la categoría seleccionada en el Spinner
                spnCategoria.setSelection(obtenerPosicionCategoria(datos.getString("Categoria")));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private int obtenerPosicionCategoria(String categoria) {
        // Aquí debes implementar la lógica para obtener la posición de la categoría en el Spinner
        // Por ejemplo, puedes recorrer el Spinner y comparar los valores
        for (int i = 0; i < spnCategoria.getCount(); i++) {
            if (spnCategoria.getItemAtPosition(i).toString().equals(categoria)) {
                return i;
            }
        }
        return 0; // Devuelve 0 si no se encuentra la categoría
    }


    private void AbrirVentana() {
        Intent intent = new Intent(this, lista_gastos.class);
        startActivity(intent);

    }

    private void guardarAmigo() {
        try {
            // Obtener los valores de los campos
            tempVal = findViewById(R.id.txtfechaGasto);
            String fecha = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtconceptoGasto);
            String concepto = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtTotal);
            String total = tempVal.getText().toString();

            // Obtener la categoría seleccionada del Spinner
            String categoria = spnCategoria.getSelectedItem().toString();

            // Validar que los campos no estén vacíos
            if (fecha.isEmpty() || concepto.isEmpty() || total.isEmpty() || categoria.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }

            // Preparar los datos para la base de datos
            String idUsuario = "1"; // ID del usuario (puedes obtenerlo dinámicamente si es necesario)
            String[] datos = {idGasto, idUsuario, categoria, fecha, concepto, total};

            // Guardar en la base de datos
            String resultado = db.administrar_gastos(accion, datos);

            // Mostrar mensaje de éxito o error
            if (resultado.equals("ok")) {
                Toast.makeText(this, "Registro guardado con éxito.", Toast.LENGTH_LONG).show();
                AbrirVentana();
            } else {
                Toast.makeText(this, "Error al guardar: " + resultado, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}