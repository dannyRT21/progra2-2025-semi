package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

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


        btn = findViewById(R.id.btnVerGraficosdesdeGastos);
        btn.setOnClickListener(view -> AbrirClaseGraficos());

        btn = findViewById(R.id.btnAgregarIngreso);
        btn.setOnClickListener(view -> AbrirClaseAgregarIngreso());

        btn = findViewById(R.id.btnAgregarGasto);
        btn.setOnClickListener(view -> AbrirClaseAgregarGasto());


        img = findViewById(R.id.imgFotoFactura);
        img.setImageResource(R.mipmap.ic_launcher_round); // Imagen por defecto

        fab = findViewById(R.id.fabVerGastos);
        fab.setOnClickListener(view -> AbrirVentana());

        spnCategoria = findViewById(R.id.spncategoria);

        mostrarDatos();
        tomarfoto();



        img.setOnClickListener(view -> mostrarOpciones());

        // Mostrar nombre del usuario
        int idUsuario = getIntent().getIntExtra("idUsuario", -1);
        TextView txtNombreUsuario = findViewById(R.id.txtNombredeUsuarioPrincipal);
        if (idUsuario == -1) {
            txtNombreUsuario.setText("Usuario no identificado");
        } else {
            DB dbHelper = new DB(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT nombre FROM usuarios WHERE idUsuario = ?", new String[]{String.valueOf(idUsuario)});
            if (cursor.moveToFirst()) {
                txtNombreUsuario.setText(cursor.getString(0));
            } else {
                txtNombreUsuario.setText("Usuario no identificado");
            }
            cursor.close();
            db.close();
        }

        // Botón salir
        setupExitButton();
    }

    private void setupExitButton() {
        Button btnExit = findViewById(R.id.btnCerrarSesion);
        btnExit.setOnClickListener(v -> showExitConfirmation());
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar aplicación")
                .setMessage("¿Estás seguro que deseas salir?")
                .setPositiveButton("Sí", (dialog, which) -> closeApp())
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void closeApp() {
        finishAffinity(); // Cierra todas las actividades
        System.exit(0);   // Finaliza el proceso
    }

    private Void AbrirClaseAgregarIngreso() {
        Intent intent = new Intent(this, agregar_ingresos.class);
        startActivity(intent);
        return null;
    }

    private Void AbrirClaseAgregarGasto() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return null;
    }

    private Void AbrirClaseGraficos() {
        Intent intent = new Intent(this, Graficos.class);
        startActivity(intent);
        return null;
    }

    private void mostrarOpciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar opción")
                .setItems(new CharSequence[]{"Tomar foto", "Seleccionar de la galería", "Cancelar"}, (dialog, which) -> {
                    switch (which) {
                        case 0: tomarfoto(); break;
                        case 1: seleccionarDeGaleria(); break;
                        case 2: dialog.dismiss(); break;
                    }
                });
        builder.create().show();
    }

    private void mostrarDatos() {
        try {
            Bundle parametros = getIntent().getExtras();
            if (parametros == null || !parametros.containsKey("accion")) {
                accion = "nuevo";
                idGasto = "";
                tempVal = findViewById(R.id.txtfechaGasto); tempVal.setText("");
                tempVal = findViewById(R.id.txtconceptoGasto); tempVal.setText("");
                tempVal = findViewById(R.id.txtTotal); tempVal.setText("");
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
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            mostrarMsg("Permiso de cámara denegado.");
        }
    }

    private void abrirCamara() {
        tomarfotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File fotoFactura = crearImagenFactura();
            if (fotoFactura != null) {
                Uri uriFotoFactura = FileProvider.getUriForFile(this,
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
                if (requestCode == 1 && urlCompletaFoto != null) {
                    File file = new File(urlCompletaFoto);
                    if (file.exists()) {
                        img.setImageURI(Uri.fromFile(file));
                    } else {
                        mostrarMsg("Archivo de imagen no encontrado.");
                    }
                } else if (requestCode == 2 && data != null) {
                    Uri imagenSeleccionada = data.getData();
                    String rutaAbsoluta = obtenerRutaAbsoluta(imagenSeleccionada);
                    if (rutaAbsoluta != null) {
                        urlCompletaFoto = rutaAbsoluta;
                        img.setImageURI(Uri.parse(urlCompletaFoto));
                    } else {
                        mostrarMsg("No se pudo obtener la ruta de la imagen seleccionada.");
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
        String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "imagen_" + fechaHora + "_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
        File image = File.createTempFile(nombreArchivo, ".jpg", directorio);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private int obtenerPosicionCategoria(String categoria) {
        for (int i = 0; i < spnCategoria.getCount(); i++) {
            if (spnCategoria.getItemAtPosition(i).toString().equals(categoria)) {
                return i;
            }
        }
        return 0;
    }

    private void AbrirVentana() {
        Intent intent = new Intent(this, lista_gastos.class);
        startActivity(intent);
    }

    private void guardarAmigo() {
        try {
            tempVal = findViewById(R.id.txtfechaGasto);
            String fecha = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtconceptoGasto);
            String concepto = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtTotal);
            String total = tempVal.getText().toString();

            String categoria = spnCategoria.getSelectedItem().toString();

            if (fecha.isEmpty() || concepto.isEmpty() || total.isEmpty() || categoria.isEmpty()) {
                mostrarMsg("Por favor, complete todos los campos.");
                return;
            }

            String idUsuario = "1";
            Bundle parametros = getIntent().getExtras();
            if (parametros != null && parametros.containsKey("idUsuario")) {
                idUsuario = String.valueOf(parametros.getInt("idUsuario"));
            }

            String[] datos = {idGasto, idUsuario, categoria, fecha, concepto, total, urlCompletaFoto};
            String resultado = db.administrar_gastos(accion, datos);

            if (resultado.equals("ok")) {
                mostrarMsg("Registro guardado con éxito.");
                AbrirVentana();
            } else {
                mostrarMsg("Error al guardar: " + resultado);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
}
