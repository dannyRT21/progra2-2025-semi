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
Spinner spn;
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

        // Evento del botón
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtener valores de los campos (si están vacíos, asignar 0)
                    double num1 = txtNum1.getText().toString().isEmpty() ? 0 : Double.parseDouble(txtNum1.getText().toString());
                    double num2 = txtNum2.getText().toString().isEmpty() ? 0 : Double.parseDouble(txtNum2.getText().toString());
                    double resultado = 0.0;
                    spn = findViewById(R.id.spnOpciones);
                    switch (spn.getSelectedItemPosition())
                    {
                        case 0 :
                            resultado = num1 + num2;
                            break;

                        case 1 :
                            resultado = num1 - num2;
                            break;

                        case 2:
                            resultado = num1 *num2;
                            break;

                        case 3:
                            resultado = num1 /num2;
                            break;

                        case 4:
                            resultado = Math.pow(num1,num2);
                            break;

                        case 5:
                            resultado = (num1 * num2) / 100;
                            break;
                        case 6: // Raíz cuadrada
                            if (num1 < 0) {
                                lblRespuesta.setText("Error:la raíz de un número negativo no existe.");
                                return;
                            }
                            resultado = Math.sqrt(num1);
                            break;
                        case 7: // Factorial
                            if (num1 < 0 || num1 != (int) num1) {
                                lblRespuesta.setText("Error: Factorial se calcula a enteros positivos.");
                                return;
                            }
                            int n = (int) num1;
                            resultado = 1;
                            for (int i = 1; i <= n; i++) {
                                resultado *= i;
                            }
                            break;
                        default:
                            lblRespuesta.setText("Error: la Operación no da.");
                            return;
                    }




                    lblRespuesta.setText("Respuesta: " + resultado);
                } catch (NumberFormatException e) {
                    lblRespuesta.setText("Error: solo podes ingresar numeros.");
                }
            }
        });
    }
}

