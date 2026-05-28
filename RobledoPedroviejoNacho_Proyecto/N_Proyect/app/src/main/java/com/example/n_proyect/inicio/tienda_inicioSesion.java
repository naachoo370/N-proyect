package com.example.n_proyect.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.R;
import com.example.n_proyect.Cliente.cliente_PantallaPrincipal;
import com.example.n_proyect.Admin.admin_pantallaPrincipal;
import com.google.android.material.button.MaterialButton;

public class tienda_inicioSesion extends AppCompatActivity {

    // Variables xml
    private EditText editTextCorreo, editTextContraseña;
    private MaterialButton botonIniciarSesion;
    private GestorN_Proyect gestor;
    private ControlSesion administradorSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda_iniciosesion); // Cargar XML

        administradorSesion = new ControlSesion(this); // iniciar el control de sesion

        // si hay sesion inciada, depende del rol uno o otra
        String correoGuardado = administradorSesion.obtenerCorreo();
        if (correoGuardado != null) {
            gestor = new GestorN_Proyect(this);
            String rol = gestor.obtenerRol(correoGuardado);
            Intent intent;
            if ("admin".equals(rol)) {
                intent = new Intent(tienda_inicioSesion.this, admin_pantallaPrincipal.class); // pantalla de admin
            } else {
                intent = new Intent(tienda_inicioSesion.this, cliente_PantallaPrincipal.class); //pantalla de cliente
            }
            intent.putExtra("CORREO_USUARIO", correoGuardado); // pasamos el correo
            startActivity(intent);
            finish();
            return;
        }

        // ir a registro
        TextView enlaceRegistro = findViewById(R.id.txt_ir_registro);
        enlaceRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tienda_inicioSesion.this, tienda_registro.class)); // Ir a pantalla de registro
            }
        });

        // enlazamos el xml con su boton
        editTextCorreo = findViewById(R.id.edt_correo);
        editTextContraseña = findViewById(R.id.edt_contrasena);
        botonIniciarSesion = findViewById(R.id.btn_login);

        gestor = new GestorN_Proyect(this); // pasamos la bd

        // bton inciar sesion
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString();
                String contrasena = editTextContraseña.getText().toString();

                // ver si los campos estan vacios o no
                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(tienda_inicioSesion.this, "Por favor, ingresa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // esta o no esta en la base de datos
                boolean usuarioValido = gestor.verificarUsuario(correo, contrasena);
                if (usuarioValido) {
                    String rol = gestor.obtenerRol(correo);
                    if (rol != null) {
                        administradorSesion.guardarSesion(correo);

                        // segun el rol uno o otro
                        Intent intent;
                        if (rol.equals("admin")) {
                            intent = new Intent(tienda_inicioSesion.this, admin_pantallaPrincipal.class);
                        } else {
                            intent = new Intent(tienda_inicioSesion.this, cliente_PantallaPrincipal.class);
                        }
                        intent.putExtra("CORREO_USUARIO", correo);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // contraseñas incorrectas
                    Toast.makeText(tienda_inicioSesion.this, "Contraseñas incorrectas.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
