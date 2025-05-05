package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_vista extends AppCompatActivity {
    private EditText etUsuario, etPassword;
    private Button btnIngresar,btnRegistrarNuevo;
    private DB dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vista);

        etUsuario = findViewById(R.id.lbltUsuario);
        etPassword = findViewById(R.id.lblContraseña);
        btnIngresar = findViewById(R.id.btnLogin);
        btnRegistrarNuevo = findViewById(R.id.btnRegistrarNuevo);
        dbHelper = new DB(this);

        btnIngresar.setOnClickListener(view -> ingresar());

        btnRegistrarNuevo.setOnClickListener(view -> {
            Intent intent = new Intent(this, registrar_user.class);
            startActivity(intent);
        });

    }

    private void ingresar() {
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            return;
        }

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(
                     "SELECT * FROM usuarios WHERE usuario = ? AND clave = ?",
                     new String[]{usuario, password})) {

            if (cursor.moveToFirst()) {
                int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")); // Obtén el nombre del usuario

                Toast.makeText(this, "¡Bienvenido, " + nombre + "!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("idUsuario", idUsuario);
                intent.putExtra("nombreUsuario", nombre); // Pasa el nombre
                intent.putExtra("claveUsuario", password); // Pasa la contraseña
                startActivity(intent);
                finish();
            } else {
                etPassword.setText("");
                Toast.makeText(this, "Usuario o contraseña incorrectos. Intente de nuevo.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error en la base de datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}