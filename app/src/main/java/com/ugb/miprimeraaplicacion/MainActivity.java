package com.ugb.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    double num1 = txtNum1.getText().toString().isEmpty() ? 0 : Double.parseDouble(txtNum1.getText().toString());
                    double num2 = txtNum2.getText().toString().isEmpty() ? 0 : Double.parseDouble(txtNum2.getText().toString());
                    double resultado = 0.0;
                    String msg = "";
                    spn = findViewById(R.id.spnOpciones);
                    switch (spn.getSelectedItemPosition()) {
                        case 0:
                            resultado = num1 + num2;
                            msg = "La suma es: " + resultado;
                            break;

                        case 1:
                            resultado = num1 - num2;
                            msg = "La resta es: " + resultado;
                            break;

                        case 2:
                            resultado = num1 * num2;
                            msg = "La multiplicación es: " + resultado;
                            break;

                        case 3:
                            resultado = num1 / num2;
                            msg = "La división es: " + resultado;
                            break;

                        case 4:
                            resultado = Math.pow(num2, num1);
                            msg = "El exponente de " + num1 + " y " + num2 + " es igual a: " + resultado;
                            break;

                        case 5:
                            resultado = (num1 * num2) / 100;
                            msg = "El porcentaje es: " + resultado;
                            break;

                        case 6: // Raíz cuadrada
                            if (num1 < 0) {
                                lblRespuesta.setText("Error: la raíz de un número negativo no existe.");
                                return;
                            }
                            resultado = Math.sqrt(num1);
                            msg = "La raíz cuadrada de " + num1 + " es igual a: " + resultado;
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
                            msg = "El factorial de " + num1 + " es igual a: " + resultado;
                            break;

                        case 8: // Mayor de dos números
                            if (num1 > num2) {
                                msg = "El número mayor es: " + num1;
                            } else if (num1 < num2) {
                                msg = "El número mayor es: " + num2;
                            } else {
                                msg = "Ambos números son iguales.";
                            }
                            break;

                        case 9: // Módulo (residuo)
                            resultado = num1 % num2;
                            msg = "El módulo (residuo) de " + num1 + " y " + num2 + " es: " + resultado;
                            break;

                        default:
                            lblRespuesta.setText("Error: la operación no es válida.");
                            return;
                    }
                    lblRespuesta.setText(msg);
                } catch (Exception e) {
                    lblRespuesta.setText("Error: " + e.getMessage());
                }
            }
        });
    }
}