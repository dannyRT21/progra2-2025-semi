package com.ugb.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    Button btn;
    TextView tempVal;
    Spinner spn;
    conversores objConversores = new conversores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tabMonedas).setIndicator("MONEDAS", null));
        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tabLongitud).setIndicator("LONGITUD", null));
        tbh.addTab(tbh.newTabSpec("Tiempo").setContent(R.id.tabTiempo).setIndicator("TIEMPO", null));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tabAlmacenamiento).setIndicator("ALMACENAMIENTO", null));
        tbh.addTab(tbh.newTabSpec("Transferenciadedatos").setContent(R.id.tabTransferenciadedatos).setIndicator("TRANSFERENCIA DE DATOS", null));

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempVal = findViewById(R.id.txtCantidad);
                String cantidadTexto = tempVal.getText().toString();

                // Validación para verificar si el campo está vacío
                if (cantidadTexto.isEmpty()) {
                    Toast.makeText(MainActivity.this, "INGRESA UN VALOR", Toast.LENGTH_SHORT).show();
                    return; // Detener la ejecución si el campo está vacío
                }

                // Validación para verificar si el usuario ingresó solo un punto o una coma
                if (cantidadTexto.equals(".") || cantidadTexto.equals(",")) {
                    Toast.makeText(MainActivity.this, "Caracter no válido", Toast.LENGTH_SHORT).show();
                    return; // Detener la ejecución si el campo contiene solo un punto o una coma
                }

                int opcion = tbh.getCurrentTab(); // Obtener la pestaña actual
                double cantidad = Double.parseDouble(cantidadTexto);

                double respuesta = 0;
                String unidad = "";

                switch (opcion) {
                    case 0: // Monedas
                        int deMonedas = ((Spinner) findViewById(R.id.spnDeMonedas)).getSelectedItemPosition();
                        int aMonedas = ((Spinner) findViewById(R.id.spnAMonedas)).getSelectedItemPosition();

                        // Validación para evitar conversiones de la misma unidad
                        if (deMonedas == aMonedas) {
                            Toast.makeText(MainActivity.this, "No se pueden convertir dos monedas iguales", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        respuesta = objConversores.convertir(opcion, deMonedas, aMonedas, cantidad);
                        unidad = ((Spinner) findViewById(R.id.spnAMonedas)).getSelectedItem().toString();
                        break;

                    case 1: // Longitud
                        int deLongitud = ((Spinner) findViewById(R.id.spnDeLongitud)).getSelectedItemPosition();
                        int aLongitud = ((Spinner) findViewById(R.id.spnALongitud)).getSelectedItemPosition();

                        // Validación para evitar conversiones de la misma unidad
                        if (deLongitud == aLongitud) {
                            Toast.makeText(MainActivity.this, "No se pueden convertir dos unidades de longitud iguales", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        respuesta = objConversores.convertir(opcion, deLongitud, aLongitud, cantidad);
                        unidad = ((Spinner) findViewById(R.id.spnALongitud)).getSelectedItem().toString();
                        break;

                    case 2: // Tiempo
                        int deTiempo = ((Spinner) findViewById(R.id.spnATiempo)).getSelectedItemPosition();
                        int aTiempo = ((Spinner) findViewById(R.id.spnDeTiempo)).getSelectedItemPosition();

                        // Validación para evitar conversiones de la misma unidad
                        if (deTiempo == aTiempo) {
                            Toast.makeText(MainActivity.this, "No se pueden convertir dos unidades de Tiempo iguales", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        respuesta = objConversores.convertir(opcion, deTiempo, aTiempo, cantidad);
                        unidad = ((Spinner) findViewById(R.id.spnATiempo)).getSelectedItem().toString();
                        break;

                    case 3: // Almacenamiento
                        int deAlmacenamiento = ((Spinner) findViewById(R.id.spnDeAlmacenamiento)).getSelectedItemPosition();
                        int aAlmacenamiento = ((Spinner) findViewById(R.id.spnAAlmacenamiento)).getSelectedItemPosition();

                        // Validación para evitar conversiones de la misma unidad
                        if (deAlmacenamiento == aAlmacenamiento) {
                            Toast.makeText(MainActivity.this, "No se pueden convertir dos unidades de Almacenamiento iguales", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        respuesta = objConversores.convertir(opcion, deAlmacenamiento, aAlmacenamiento, cantidad);
                        unidad = ((Spinner) findViewById(R.id.spnAAlmacenamiento)).getSelectedItem().toString();
                        break;
                    case 4: // Transferencia de datos
                        int deTransferencia = ((Spinner) findViewById(R.id.spnDeTransferencia)).getSelectedItemPosition();
                        int aTransferencia = ((Spinner) findViewById(R.id.spnATransferencia)).getSelectedItemPosition();

                        // Validación para evitar conversiones de la misma unidad
                        if (deTransferencia == aTransferencia) {
                            Toast.makeText(MainActivity.this, "No se pueden convertir dos unidades de Tranferencia de datos iguales", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        respuesta = objConversores.convertir(opcion, deTransferencia, aTransferencia, cantidad);
                        unidad = ((Spinner) findViewById(R.id.spnATransferencia)).getSelectedItem().toString();
                        break;
                }

                // Mostrar la respuesta
                tempVal = findViewById(R.id.lblRespuesta);
                tempVal.setText(String.format("Respuesta: %.2f %s", respuesta, unidad));
            }
        });
    }
    public void limpiarCampoRespuesta(View view) {
        // Limpiar el campo de respuesta (lblRespuesta)
        TextView lblRespuesta = findViewById(R.id.lblRespuesta);
        lblRespuesta.setText(""); // Limpia el contenido del campo de respuesta

        // Limpiar el campo de cantidad (txtCantidad)
        TextView txtCantidad = findViewById(R.id.txtCantidad);
        txtCantidad.setText(""); // Limpia el contenido del campo de cantidad
    }
}

class conversores {
    double[][] valores = {
            // Monedas
            {1, 0.98, 7.73, 25.45, 36.78, 508.87, 8.74, 0.0087, 0.0073, 0.0054, 0.049}, // Monedas
            // Longitud (metros, kilómetros, millas, pies, centímetros, milímetros, yardas, pulgadas, hectáreas, nanómetros)
            {1, 0.001, 0.000621371, 3.28084, 100, 1000, 1.09361, 39.3701, 0.0001, 1e9}, // Longitud
            // Tiempo (segundos, minutos, horas, días, semanas, meses, años, décadas, siglos, milenios)
            {1, 60, 3600, 86400, 604800, 2.628e+6, 3.154e+7, 3.154e+8, 3.154e+9, 3.154e+10}, // Tiempo
            // Almacenamiento (byte, kilobytes, megabytes, gigabytes, terabytes, petabytes, exabytes, zettabytes, yottabytes, brontobytes)
            {1, 0.001, 0.000001, 0.000000001, 0.000000000001, 0.000000000000001, 0.000000000000000001, 0.000000000000000000001, 0.000000000000000000000001, 0.000000000000000000000000001}, // Almacenamiento
            // Transferencia de datos (bits, kilobits, megabits, gigabits, terabits, petabits, exabits, zettabits, yottabits, brontobits)
            {1, 0.001, 0.000001, 0.000000001, 0.000000000001, 0.000000000000001, 0.000000000000000001, 0.000000000000000000001, 0.000000000000000000000001, 0.000000000000000000000000001}, // Transferencia de datos
    };

    public double convertir(int opcion, int de, int a, double cantidad) {
        return (valores[opcion][a] / valores[opcion][de]) * cantidad;
    }

}