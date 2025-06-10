package com.ugb.miprimeraaplicacion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.Calendar;

public class agregar_ingresos extends AppCompatActivity {

    private FloatingActionButton fabVerIngresos;
    private Button btnGuardarIngreso, btnVerGraficos, btnAgregarGasto, btnSalir;

    private EditText txtFuenteDeIngreso, txtFechaIngreso, txtDescripcionIngreso, txtMontoIngreso;
    private DB db;

    private String accion = "nuevo";
    private String idIngreso = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ingresos);

        db = new DB(this);


        btnGuardarIngreso = findViewById(R.id.btnGuardarIngreso);
        btnVerGraficos = findViewById(R.id.btnVerGraficos);
        btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        fabVerIngresos = findViewById(R.id.fabVerIngresos);
        btnSalir = findViewById(R.id.btnsalir);

        btnGuardarIngreso.setOnClickListener(view -> guardarIngreso());
        btnVerGraficos.setOnClickListener(view -> abrirClaseGraficos());
        btnAgregarGasto.setOnClickListener(view -> abrirClaseGastos());
        fabVerIngresos.setOnClickListener(view -> abrirVentanaDeIngreso());
        btnSalir.setOnClickListener(view -> mostrarConfirmacionSalida());


        txtFuenteDeIngreso = findViewById(R.id.txtFuentedeIngreso);
        txtFechaIngreso = findViewById(R.id.txtfechaIngreso);
        txtDescripcionIngreso = findViewById(R.id.txtdescripcionIngreso);
        txtMontoIngreso = findViewById(R.id.txtMontoIngreso);


        txtFechaIngreso.setInputType(InputType.TYPE_NULL);
        txtFechaIngreso.setOnClickListener(view -> mostrarDatePicker());

        mostrarDatosDeIngresos();
    }

    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                agregar_ingresos.this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, (month + 1), year);
                    txtFechaIngreso.setText(fechaSeleccionada);
                },
                anio, mes, dia
        );
        datePicker.show();
    }

    private void abrirClaseGraficos() {
        Intent intent = new Intent(this, Graficos.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void abrirClaseGastos() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void abrirVentanaDeIngreso() {
        Intent intent = new Intent(this, IngresosLista.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarDatosDeIngresos() {
        try {
            Bundle parametros = getIntent().getExtras();
            if (parametros == null || !parametros.containsKey("accion")) {
                accion = "nuevo";
                idIngreso = "";
                limpiarCampos();
                return;
            }

            accion = parametros.getString("accion");
            if ("modificar".equals(accion)) {
                JSONObject datos = new JSONObject(parametros.getString("ingresos"));
                idIngreso = datos.getString("IdIngreso");

                txtFuenteDeIngreso.setText(datos.getString("Categoria"));
                txtFechaIngreso.setText(datos.getString("Fecha"));
                txtDescripcionIngreso.setText(datos.getString("Concepto"));
                txtMontoIngreso.setText(datos.getString("Total"));
            } else {
                accion = "nuevo";
                idIngreso = "";
                limpiarCampos();
            }
        } catch (Exception e) {
            Log.e("agregar_ingresos", "Error al obtener datos", e);
            mostrarMsg("Error al obtener los datos: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtFuenteDeIngreso.setText("");
        txtFechaIngreso.setText("");
        txtDescripcionIngreso.setText("");
        txtMontoIngreso.setText("");
    }

    private void guardarIngreso() {
        try {
            String categoria = txtFuenteDeIngreso.getText().toString().trim();
            String fecha = txtFechaIngreso.getText().toString().trim();
            String concepto = txtDescripcionIngreso.getText().toString().trim();
            String total = txtMontoIngreso.getText().toString().trim();

            if (categoria.isEmpty() || fecha.isEmpty() || concepto.isEmpty() || total.isEmpty()) {
                mostrarMsg("Por favor, complete todos los campos.");
                return;
            }

            String idUsuario = "1";
            Bundle parametros = getIntent().getExtras();
            if (parametros != null && parametros.containsKey("idUsuario")) {
                idUsuario = String.valueOf(parametros.getInt("idUsuario"));
            }

            String[] datos = {idIngreso, idUsuario, categoria, fecha, concepto, total};
            String resultado = db.administrar_ingresos(accion, datos);

            if ("ok".equals(resultado)) {
                mostrarMsg("Ingreso guardado con éxito.");
                Intent intent = new Intent(this, IngresosLista.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                mostrarMsg("Error al guardar: " + resultado);
            }
        } catch (Exception e) {
            Log.e("agregar_ingresos", "Error al guardar ingreso", e);
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void mostrarConfirmacionSalida() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Estás seguro que deseas salir sin guardar?")
                .setPositiveButton("Sí", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}
