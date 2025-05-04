package com.ugb.miprimeraaplicacion;

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
    private Button btnIngresar;
    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vista);

        etUsuario = findViewById(R.id.lbltUsuario);
        etPassword = findViewById(R.id.lblContraseña);
        btnIngresar = findViewById(R.id.btnLogin);
        dbHelper = new DB(this);

        btnIngresar.setOnClickListener(view -> ingresar());
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
                // Login successful
                Toast.makeText(this, "¡Bienvenido, " + usuario + "!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, registrar_user.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            } else {
                // Login failed
                etPassword.setText("");
                Toast.makeText(this, "Usuario o contraseña incorrectos. Intente de nuevo.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error en la base de datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}