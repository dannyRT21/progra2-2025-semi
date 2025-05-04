package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Button btn;
    Spinner spnCategoria;
    TextView tempVal;
    DB db;
    String accion = "nuevo", idGasto = "";

    ImageView img;

    String urlCompletaFoto = "";

    Intent tomarfotoIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DB(this);
        btn = findViewById(R.id.btnguardarGasto);
        btn.setOnClickListener(View -> guardarAmigo());
        img = findViewById(R.id.imgFotoFactura);
        fab = findViewById(R.id.fabVerGastos);
        fab.setOnClickListener(view -> AbrirVentana());
        spnCategoria = findViewById(R.id.spncategoria);


        mostrarDatos();
        tomarfoto();
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

                urlCompletaFoto = datos.getString("UrlFoto");
                img.setImageURI(Uri.parse(urlCompletaFoto));


                // Aquí puedes establecer la categoría seleccionada en el Spinner
                spnCategoria.setSelection(obtenerPosicionCategoria(datos.getString("Categoria")));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void tomarfoto() {
        img.setOnClickListener(view ->  {
            tomarfotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoFactura = null;
            try{
            fotoFactura = crearImagenFactura();
            if (fotoFactura!=null){
                Uri uriFotoFactura = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.miprimeraaplicacion.fileprovider",fotoFactura);
                tomarfotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoFactura);
                startActivityForResult(tomarfotoIntent, 1);


            }else {
                mostrarMsg("Error al crear la imagen");
            }
            }catch (Exception e){
               mostrarMsg("Error: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                //Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageURI(Uri.parse(urlCompletaFoto));

            } else {
                mostrarMsg("Error al tomar la foto");
            }

        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private File crearImagenFactura() throws Exception {
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs+"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }


    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
            String[] datos = {idGasto, idUsuario, categoria, fecha, concepto, total, urlCompletaFoto};

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