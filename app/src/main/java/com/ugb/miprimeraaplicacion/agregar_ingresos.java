package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class agregar_ingresos extends AppCompatActivity {

    FloatingActionButton fab;
    Button btn;

    TextView tempVal;
    DB db;
    String accion = "nuevo", IdIngreso = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ingresos);

        db = new DB(this);

        btn = findViewById(R.id.btnGuardarIngreso);
        btn.setOnClickListener(View -> guardarIngreso());


        fab = findViewById(R.id.fabVerIngresos);
        fab.setOnClickListener(view -> AbrirVentanadeIngreso());

        mostrarDatosdeIngresos();

    }

    private void mostrarDatosdeIngresos() {
        try {
            Bundle parametros = getIntent().getExtras();
            if (parametros == null || !parametros.containsKey("accion"))  {
                accion = "nuevo";
                IdIngreso = "";
                tempVal = findViewById(R.id.txtFuentedeIngreso); tempVal.setText("");
                tempVal = findViewById(R.id.txtfechaIngreso); tempVal.setText("");
                tempVal = findViewById(R.id.txtdescripcionIngreso); tempVal.setText("");
                tempVal = findViewById(R.id.txtMontoIngreso); tempVal.setText("");
                return;
            }
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("ingresos"));
                IdIngreso = datos.getString("IdIngreso");

                tempVal = findViewById(R.id.txtFuentedeIngreso);
                tempVal.setText(datos.getString("Categoria"));

                tempVal = findViewById(R.id.txtfechaIngreso);
                tempVal.setText(datos.getString("Fecha"));

                tempVal = findViewById(R.id.txtdescripcionIngreso);
                tempVal.setText(datos.getString("Concepto"));

                tempVal = findViewById(R.id.txtMontoIngreso);
                tempVal.setText(datos.getString("Total"));
            } else {
                accion = "nuevo";
                IdIngreso = "";
                tempVal = findViewById(R.id.txtFuentedeIngreso); tempVal.setText("");
                tempVal = findViewById(R.id.txtfechaIngreso); tempVal.setText("");
                tempVal = findViewById(R.id.txtdescripcionIngreso); tempVal.setText("");
                tempVal = findViewById(R.id.txtMontoIngreso); tempVal.setText("");
            }
        }
        catch (Exception e) {
            mostrarMsg("Error al obtener los datos: " + e.getMessage());
            return;
        }
    }

    private void AbrirVentanadeIngreso() {
        Intent intent = new Intent(this, IngresosLista.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void guardarIngreso() {
        try {
            TextView tempVal;

            tempVal = findViewById(R.id.txtFuentedeIngreso);
            String Categoria = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtfechaIngreso);
            String Fecha = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtdescripcionIngreso);
            String Concepto = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtMontoIngreso);
            String Total = tempVal.getText().toString();

            if (Categoria.isEmpty() || Fecha.isEmpty() || Concepto.isEmpty() || Total.isEmpty()) {
                mostrarMsg("Por favor, complete todos los campos.");
                return;
            }

            String idUsuario = "1";
            Bundle parametros = getIntent().getExtras();
            if (parametros != null && parametros.containsKey("idUsuario")) {
                idUsuario = String.valueOf(parametros.getInt("idUsuario"));
            }

            String[] datos = {IdIngreso, idUsuario, Categoria, Fecha, Concepto, Total};
            String resultado = db.administrar_ingresos(accion, datos);

            if (resultado.equals("ok")) {
                mostrarMsg("Ingreso guardado con éxito.");
                Intent intent = new Intent(this, IngresosLista.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                mostrarMsg("Error al guardar: " + resultado);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}