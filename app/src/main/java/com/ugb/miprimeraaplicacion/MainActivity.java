package com.ugb.miprimeraaplicacion;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorLuz();
    }
    @Override
    protected void onResume() { //estos metodos se llaman haciendo click derecho y en la opcion Generate/Override
        iniciar();
        super.onResume();
    }
    @Override
    protected void onPause() {
        detener();
        super.onPause();
    }
    private void iniciar(){
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000); //es la cantidad de tiempo de ejecucion de este sensor
    }
    private void detener(){
        sensorManager.unregisterListener(sensorEventListener);
    }
    private void sensorLuz(){
        tempVal = findViewById(R.id.lblSensorLuz);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //administrador de sensores

        //if else para condicionar el funcionamiento del censor de luz
        if( sensor==null ){
            tempVal.setText("Tu dispositivo, NO tiene el senor de LUZ");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                double valor = event.values[0];
                tempVal.setText("Cantidad de Luz: "+ valor);

                if(valor<=20){
                    getWindow().getDecorView().setBackgroundColor(Color.GRAY);
                }else if(valor<=50){
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }else{
                    getWindow().getDecorView().setBackgroundColor(Color.MAGENTA);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
}