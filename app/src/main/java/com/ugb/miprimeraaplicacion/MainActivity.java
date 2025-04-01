package com.ugb.miprimeraaplicacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
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
    TextView tempVal;
    DB db;
    String accion = "nuevo", idAmigo = "";
    ImageView img;
    String urlCompletaFoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imgFotoAmigo);
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(view->guardarAmigo());

        fab = findViewById(R.id.fabListaAmigos);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
        seleccionarImagen();
    }

    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            if(parametros != null) {
                accion = parametros.getString("accion");
                if (accion.equals("modificar")) {
                    JSONObject datos = new JSONObject(parametros.getString("amigos"));
                    idAmigo = datos.getString("idAmigo");

                    tempVal = findViewById(R.id.txtNombre);
                    tempVal.setText(datos.getString("nombre"));

                    tempVal = findViewById(R.id.txtDireccion);
                    tempVal.setText(datos.getString("direccion"));

                    tempVal = findViewById(R.id.txtTelefono);
                    tempVal.setText(datos.getString("telefono"));

                    tempVal = findViewById(R.id.txtEmail);
                    tempVal.setText(datos.getString("email"));

                    tempVal = findViewById(R.id.txtDui);
                    tempVal.setText(datos.getString("dui"));

                    urlCompletaFoto = datos.getString("urlFoto");
                    if(urlCompletaFoto != null && !urlCompletaFoto.isEmpty()) {
                        img.setImageURI(Uri.parse(urlCompletaFoto));
                    }
                }
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }

    private void seleccionarImagen(){
        img.setOnClickListener(view->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                urlCompletaFoto = selectedImage.toString();
                img.setImageURI(selectedImage);
            } else {
                mostrarMsg("No se seleccionó ninguna imagen.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirVentana(){
        Intent intent = new Intent(this, lista_amigos.class);
        startActivity(intent);
    }

    private void guardarAmigo() {
        try {
            tempVal = findViewById(R.id.txtNombre);
            String nombre = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDireccion);
            String direccion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtTelefono);
            String telefono = tempVal.getText().toString();
            tempVal = findViewById(R.id.txtEmail);
            String email = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDui);
            String dui = tempVal.getText().toString();



            String[] datos = {idAmigo, nombre, direccion, telefono, email, dui, urlCompletaFoto};
            db.administrar_amigos(accion, datos);
            Toast.makeText(getApplicationContext(), "Registro guardado con exito.", Toast.LENGTH_LONG).show();
            abrirVentana();
        } catch (Exception e) {
            mostrarMsg("Error al guardar: " + e.getMessage());
        }
    }
}