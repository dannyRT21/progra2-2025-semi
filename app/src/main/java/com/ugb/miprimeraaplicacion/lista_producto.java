package com.ugb.miprimeraaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class lista_producto extends Activity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_producto);
        fab = findViewById(R.id.fabRegresarProducto);
        fab.setOnClickListener(view -> {
            AbrirVentana();
        });
    }
    private  void  AbrirVentana(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
