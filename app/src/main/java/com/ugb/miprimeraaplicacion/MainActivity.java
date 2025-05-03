package com.ugb.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnGuardar;
    Spinner spnCategoria;
    EditText txtFechaGasto, txtConceptoGasto, txtTotal;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos
        db = new DB(this);

        // Referencias a los elementos de la interfaz
        spnCategoria = findViewById(R.id.spncategoria);
        txtFechaGasto = findViewById(R.id.txtfechaGasto);
        txtConceptoGasto = findViewById(R.id.txtconceptoGasto);
        txtTotal = findViewById(R.id.txtTotal);
        btnGuardar = findViewById(R.id.btnguardarGasto);

        // Configurar el botón para guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarAmigo();
            }
        });
    }

    private void guardarAmigo() {
        try {
            // Obtener los valores de los campos
            String categoria = spnCategoria.getSelectedItem().toString();
            String fecha = txtFechaGasto.getText().toString();
            String concepto = txtConceptoGasto.getText().toString();
            String total = txtTotal.getText().toString();

            // Validar que los campos no estén vacíos
            if (categoria.isEmpty() || fecha.isEmpty() || concepto.isEmpty() || total.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }

            // Preparar los datos para la base de datos
            String[] datos = {"", "1", categoria, fecha, concepto, total};

            // Guardar en la base de datos
            String resultado = db.administrar_gastos("agregar", datos);

            // Mostrar mensaje de éxito o error
            if (resultado.equals("ok")) {
                Toast.makeText(this, "Registro guardado con éxito.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al guardar: " + resultado, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}