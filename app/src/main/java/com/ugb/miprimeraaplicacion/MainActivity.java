package com.ugb.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView lblRespuesta;
    RadioGroup rgoOpciones;
    EditText txtNum1, txtNum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciar vistas
        btn = findViewById(R.id.btnCalcular);
        txtNum1 = findViewById(R.id.txtNum1);
        txtNum2 = findViewById(R.id.txtNum2);
        lblRespuesta = findViewById(R.id.lblRespuesta);
        rgoOpciones = findViewById(R.id.rgoOpciones);

        // Evento del botón
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtener valores de los campos (si están vacíos, asignar 0)
                    double num1 = txtNum1.getText().toString().isEmpty() ? 0 : Double.parseDouble(txtNum1.getText().toString());
                    double num2 = txtNum2.getText().toString().isEmpty() ? 0 : Double.parseDouble(txtNum2.getText().toString());
                    double resultado = 0.0;


                    int selectedId = rgoOpciones.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        lblRespuesta.setText("Error: Selecciona una operación.");
                        return;
                    }

                    // operaciones
                    if (selectedId == R.id.optsuma) {
                        resultado = num1 + num2;
                    } else if (selectedId == R.id.optresta) {
                        resultado = num1 - num2;
                    } else if (selectedId == R.id.optmultiplicacion) {
                        resultado = num1 * num2;
                    } else if (selectedId == R.id.optdivision) {
                        resultado = (num2 != 0) ? num1 / num2 : Double.NaN;
                    } else if (selectedId == R.id.optExponente) {
                        resultado = Math.pow(num1, num2);
                    } else if (selectedId == R.id.optPorcentaje) {
                        resultado = (num1 * num2) / 100;
                    } else if (selectedId == R.id.optRaiz) {
                        resultado = (num1 >= 0) ? Math.sqrt(num1) : Double.NaN;
                    } else if (selectedId == R.id.optFactorial) {
                        if (num1 < 0 || num1 != (int) num1) {
                            lblRespuesta.setText("Error: Factorial solo para enteros positivos.");
                            return;
                        }

                        // el factorial
                        int n = (int) num1;
                        long factorial = 1;
                        for (int i = 1; i <= n; i++) {
                            factorial *= i;
                        }
                        resultado = factorial;
                    }

                    lblRespuesta.setText("Respuesta: " + resultado);
                } catch (NumberFormatException e) {
                    lblRespuesta.setText("Error: solo podes ingresar numeros.");
                }
            }
        });
    }
}