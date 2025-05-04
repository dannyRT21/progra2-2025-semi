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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    private String idUsuario;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recibe el idUsuario del Intent como entero
        idUsuario = String.valueOf(getIntent().getIntExtra("idUsuario", -1));

        if (idUsuario.equals(1)) {
            Toast.makeText(this, "Error: Usuario no identificado.", Toast.LENGTH_LONG).show();
            finish(); // Cierra la actividad si no se identifica al usuario
            return;
        }

        configurarValidacionFecha();
        db = new DB(this);
        btn = findViewById(R.id.btnguardarGasto);
        btn.setOnClickListener(View -> guardarAmigo());
        img = findViewById(R.id.imgFotoFactura);
        fab = findViewById(R.id.fabVerGastos);
        fab.setOnClickListener(view -> AbrirVentana());
        spnCategoria = findViewById(R.id.spncategoria);

        mostrarDatos();
        tomarfoto();

        img.setOnClickListener(view -> mostrarOpciones());
    }
    private void mostrarOpciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar opción")
                .setItems(new CharSequence[]{"Tomar foto", "Seleccionar de la galería", "Cancelar"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Tomar foto
                            tomarfoto();
                            break;
                        case 1: // Seleccionar de la galería
                            seleccionarDeGaleria();
                            break;
                        case 2: // Cancelar
                            dialog.dismiss();
                            break;
                    }
                });
        builder.create().show();
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
            }if (urlCompletaFoto != null && !urlCompletaFoto.isEmpty()){
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }
            else {
            mostrarMsg("no se pudo cargar la imagen");
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

    private void seleccionarDeGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                // Mostrar la foto tomada
                img.setImageURI(Uri.parse(urlCompletaFoto));
            } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
                // Mostrar la imagen seleccionada de la galería
                Uri imagenSeleccionada = data.getData();
                img.setImageURI(imagenSeleccionada);
                urlCompletaFoto = imagenSeleccionada.toString(); // Guardar la URI de la imagen
            } else {
                mostrarMsg("Operación cancelada");
            }
        } catch (Exception e) {
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
    private void configurarValidacionFecha() {
        EditText txtFechaGasto = findViewById(R.id.txtfechaGasto);

        txtFechaGasto.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final String ddmmyyyy = "DDMMYYYY";
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        if (day > 31) day = 31;

                        clean = String.format(Locale.getDefault(), "%02d%02d%04d", day, mon, year);
                    }

                    clean = String.format(Locale.getDefault(), "%s/%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    txtFechaGasto.setText(current);
                    txtFechaGasto.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validar si la fecha es válida
                String fecha = txtFechaGasto.getText().toString();
                if (fecha.length() == 10) {
                    try {
                        sdf.setLenient(false);
                        sdf.parse(fecha); // Intenta parsear la fecha
                    } catch (ParseException e) {
                        txtFechaGasto.setError("Fecha inválida. Use el formato dd/MM/yyyy");
                    }
                }
            }
        });
    }

    private void guardarAmigo() {
        try {
            // Obtener los valores de los campos
            tempVal = findViewById(R.id.txtfechaGasto);
            String fecha = tempVal.getText().toString().trim();

            tempVal = findViewById(R.id.txtconceptoGasto);
            String concepto = tempVal.getText().toString().trim();

            tempVal = findViewById(R.id.txtTotal);
            String total = tempVal.getText().toString().trim();

            // Obtener la categoría seleccionada del Spinner
            String categoria = spnCategoria.getSelectedItem().toString().trim();

            // Validar que los campos no estén vacíos
            if (fecha.isEmpty() || concepto.isEmpty() || total.isEmpty() || categoria.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }

            // Validar que idUsuario no sea null
            if (idUsuario == null || idUsuario.isEmpty()) {
                Toast.makeText(this, "Error: Usuario no identificado.", Toast.LENGTH_LONG).show();
                return;
            }

            // Validar que urlCompletaFoto no sea null (opcional si es requerida)
            if (urlCompletaFoto == null || urlCompletaFoto.isEmpty()) {
                urlCompletaFoto = ""; // Asignar un valor vacío si no es obligatorio
            }

            // Preparar los datos para la base de datos
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