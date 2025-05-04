package com.ugb.miprimeraaplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class registrar_user extends AppCompatActivity {
    private EditText txtUsuario, txtnombre, txtclave, txtdireccion, txtTelefono;
    private Button btnRegistrar, btnIngresar;
    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_user);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtclave = findViewById(R.id.txtclave);
        txtnombre = findViewById(R.id.txtnombre);
        txtdireccion = findViewById(R.id.txtdireccion); // Fixed duplicate assignment
        txtTelefono = findViewById(R.id.txtTelefono);
        btnRegistrar = findViewById(R.id.btnregistrar);

        btnIngresar =  findViewById(R.id.btnIngresar);

        dbHelper = new DB(this);

        Bundle bundle = getIntent().getExtras();
        String usuario = bundle != null ? bundle.getString("usuario") : null;
        mostrarDatos(usuario);

        btnIngresar.setOnClickListener(view -> {
            // Navegar a la pantalla de inicio de sesión
            Intent intent = new Intent(this, login_vista.class);
            startActivity(intent);
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
    }

    public void guardar() {
        String usuario = txtUsuario.getText().toString().trim();
        String clave = txtclave.getText().toString().trim();
        String nombre = txtnombre.getText().toString().trim();
        String direccion = txtdireccion.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();

        // Validaciones
        if (usuario.isEmpty()) {
            Toast.makeText(this, "Debes ingresar un usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu nombre completo", Toast.LENGTH_LONG).show();
            return;
        }
        if (clave.isEmpty() || clave.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        if (direccion.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu dirección", Toast.LENGTH_LONG).show();
            return;
        }
        if (telefono.isEmpty() || telefono.length() != 8) {
            Toast.makeText(this, "Debes ingresar un teléfono válido de 8 dígitos", Toast.LENGTH_LONG).show();
            return;
        }

        // guadar wn database
        String[] datos = {usuario, clave, nombre, direccion, telefono};
        String resultado = dbHelper.administrar_usuarios("agregar", datos);

        if (resultado.equals("ok")) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
            txtUsuario.setText("");
            txtnombre.setText("");
            txtclave.setText("");
            txtdireccion.setText("");
            txtTelefono.setText("");

            // Redirigir a la actividad principal
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual
        } else {
            Toast.makeText(this, "Error al registrar: " + resultado, Toast.LENGTH_LONG).show();
        }
    }

    public void mostrarDatos(String usuario) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Consulta para obtener los datos del usuario
            Cursor fila = db.rawQuery(
                    "SELECT usuario, clave, nombre, direccion, telefono FROM usuarios WHERE usuario = ?",
                    new String[]{usuario});

            if (fila.moveToFirst()) {
                txtUsuario.setText(fila.getString(0));
                txtclave.setText(fila.getString(1));
                txtnombre.setText(fila.getString(2));
                txtdireccion.setText(fila.getString(3));
                txtTelefono.setText(fila.getString(4));
            } else {
                Toast.makeText(this, "No existen datos para este usuario", Toast.LENGTH_LONG).show();
            }
            fila.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al buscar datos", Toast.LENGTH_LONG).show();
        }
    }
    private void AbrirLoginClase() {
        Intent intent = new Intent(this, login_vista.class);
        startActivity(intent);
        finish();

    }


    }
