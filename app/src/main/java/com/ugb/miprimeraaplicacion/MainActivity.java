package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        img.setImageResource(R.mipmap.ic_launcher_round); // Imagen por defecto

        fab = findViewById(R.id.fabVerGastos);
        fab.setOnClickListener(view -> AbrirVentana());
        spnCategoria = findViewById(R.id.spncategoria);


        mostrarDatos();
        tomarfoto();

        img.setOnClickListener(view -> mostrarOpciones());
        // Obtén el idUsuario del Intent
        int idUsuario = getIntent().getIntExtra("idUsuario", -1);

        TextView txtNombreUsuario = findViewById(R.id.txtNombredeUsuarioPrincipal);

        if (idUsuario == -1) {
            txtNombreUsuario.setText("Usuario no identificado");
        } else {
            // Consulta el nombre en la base de datos
            DB dbHelper = new DB(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT nombre FROM usuarios WHERE idUsuario = ?", new String[]{String.valueOf(idUsuario)});
            if (cursor.moveToFirst()) {
                String nombreUsuario = cursor.getString(0);
                txtNombreUsuario.setText(nombreUsuario);
            } else {
                txtNombreUsuario.setText("Usuario no identificado");
            }
            cursor.close();
            db.close();
        }

// Recibe el usuario o idUsuario desde el Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("usuario")) {
                String usuario = extras.getString("usuario");
                // Aquí puedes usar el nombre de usuario
            }
            if (extras.containsKey("idUsuario")) {
                int idUsuario1 = extras.getInt("idUsuario");

            }
        }
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
            if (parametros == null || !parametros.containsKey("accion")) {
                // Si vienes del login, inicializa para nuevo gasto
                accion = "nuevo";
                idGasto = "";
                tempVal = findViewById(R.id.txtfechaGasto);
                tempVal.setText("");
                tempVal = findViewById(R.id.txtconceptoGasto);
                tempVal.setText("");
                tempVal = findViewById(R.id.txtTotal);
                tempVal.setText("");
                urlCompletaFoto = "";
                img.setImageResource(R.mipmap.ic_launcher_round);
                spnCategoria.setSelection(0);
                return;
            }
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

                if (urlCompletaFoto != null && !urlCompletaFoto.isEmpty()) {
                    File file = new File(urlCompletaFoto);
                    if (file.exists()) {
                        img.setImageURI(Uri.fromFile(file));
                    } else {
                        mostrarMsg("No se pudo cargar la imagen, archivo no encontrado.");
                        img.setImageResource(R.mipmap.ic_launcher_round);
                    }
                } else {
                    mostrarMsg("No se pudo cargar la imagen, ruta no válida.");
                    img.setImageResource(R.mipmap.ic_launcher_round);
                }

                spnCategoria.setSelection(obtenerPosicionCategoria(datos.getString("Categoria")));
            }
        } catch (Exception e) {
            mostrarMsg("Error en mostrarDatos: " + e.getMessage());
        }
    }

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @SuppressLint("NewApi")
    private void tomarfoto() {
        img.setOnClickListener(view -> {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                abrirCamara();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                mostrarMsg("Permiso de cámara denegado.");
            }
        }
    }

    private void abrirCamara() {
        tomarfotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoFactura = null;
        try {
            fotoFactura = crearImagenFactura();
            if (fotoFactura != null) {
                Uri uriFotoFactura = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.miprimeraaplicacion.fileprovider", fotoFactura);
                tomarfotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoFactura);
                startActivityForResult(tomarfotoIntent, 1);
            } else {
                mostrarMsg("Error al crear la imagen");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
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
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) { // Foto tomada con la cámara
                    if (urlCompletaFoto != null && !urlCompletaFoto.isEmpty()) {
                        File file = new File(urlCompletaFoto);
                        if (file.exists()) {
                            img.setImageURI(Uri.fromFile(file));
                        } else {
                            mostrarMsg("No se pudo cargar la imagen tomada, archivo no encontrado.");
                        }
                    } else {
                        mostrarMsg("Ruta de la imagen tomada no válida.");
                    }
                } else if (requestCode == 2 && data != null) { // Imagen seleccionada de la galería
                    Uri imagenSeleccionada = data.getData();
                    if (imagenSeleccionada != null) {
                        String rutaAbsoluta = obtenerRutaAbsoluta(imagenSeleccionada);
                        if (rutaAbsoluta != null) {
                            urlCompletaFoto = rutaAbsoluta;
                            img.setImageURI(Uri.parse(urlCompletaFoto));
                        } else {
                            mostrarMsg("No se pudo obtener la ruta de la imagen seleccionada.");
                        }
                    } else {
                        mostrarMsg("No se seleccionó ninguna imagen.");
                    }
                }
            } else {
                mostrarMsg("Operación cancelada.");
            }
        } catch (Exception e) {
            mostrarMsg("Error en onActivityResult: " + e.getMessage());
        }
    }

    private String obtenerRutaAbsoluta(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String ruta = cursor.getString(columnIndex);
            cursor.close();
            return ruta;
        }
        return null;
    }

    private File crearImagenFactura() throws Exception {
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_" + fechaHoraMs + "_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (dirAlmacenamiento.exists() == false) {
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }


    private void mostrarMsg(String msg) {
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

            String categoria = spnCategoria.getSelectedItem().toString();

            if (fecha.isEmpty() || concepto.isEmpty() || total.isEmpty() || categoria.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }

            // Obtener idUsuario desde el Intent si existe, si no, usar "1" por defecto
            String idUsuario = "1";
            Bundle parametros = getIntent().getExtras();
            if (parametros != null && parametros.containsKey("idUsuario")) {
                idUsuario = String.valueOf(parametros.getInt("idUsuario"));
            }

            String[] datos = {idGasto, idUsuario, categoria, fecha, concepto, total, urlCompletaFoto};
            String resultado = db.administrar_gastos(accion, datos);

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


