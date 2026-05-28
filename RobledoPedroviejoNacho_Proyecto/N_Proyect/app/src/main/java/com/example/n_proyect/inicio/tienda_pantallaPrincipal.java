package com.example.n_proyect.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n_proyect.R;

public class tienda_pantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda_inicio);

        // Btn inicio de sesion
        Button btnLogin = findViewById(R.id.btn_iniciar_sesion);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tienda_pantallaPrincipal.this, tienda_inicioSesion.class);
                startActivity(intent);
            }
        });

        // Btn registro
        Button btnRegister = findViewById(R.id.btn_registro);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tienda_pantallaPrincipal.this, tienda_registro.class);
                startActivity(intent);
            }
        });
    }
}
