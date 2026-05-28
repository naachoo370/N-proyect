package com.example.n_proyect.inicio;

import android.content.Intent;
import android.os.Bundle;
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

public class tienda_registro extends AppCompatActivity {

    // Variables xml
    private EditText edtCorreo, edtContraseña;
    private MaterialButton btnRegistrar;
    private TextView txtInicioSesion;
    private GestorN_Proyect gestorBD;
    private ControlSesion gestorSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda_registro); // cargar XML

        edtCorreo = findViewById(R.id.edt_registro_email);
        edtContraseña = findViewById(R.id.edt_registro_password);
        btnRegistrar = findViewById(R.id.btn_registrar);
        txtInicioSesion = findViewById(R.id.txt_login_link);

        // inciamos bd y el incio de sesion
        gestorBD = new GestorN_Proyect(this);
        gestorSesion = new ControlSesion(this);

        // accion para registar
        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        // accion para inciar sesion
        txtInicioSesion.setOnClickListener(v -> {
            Intent intent = new Intent(tienda_registro.this, tienda_inicioSesion.class);
            startActivity(intent);
            finish();
        });
    }

    private void registrarUsuario() {
        // Obtener texto ingresado por el usuario
        String correo = edtCorreo.getText().toString().trim();
        String contraseña = edtContraseña.getText().toString().trim();

        // ha ingresado correo
        if (correo.isEmpty()) {
            edtCorreo.setError("El correo es necesario");
            edtCorreo.requestFocus();
            return;
        }

        // ha ingresado contraseña
        if (contraseña.isEmpty()) {
            edtContraseña.setError("La contraseña es necesaria");
            edtContraseña.requestFocus();
            return;
        }

        // que tenga 6 carracteres como min
        if (contraseña.length() < 6) {
            edtContraseña.setError("La contraseña debe tener al menos 6 caracteres");
            edtContraseña.requestFocus();
            return;
        }

        // insert en la base de datos
        boolean exito = gestorBD.insertarUsuario(correo, contraseña);
        if (exito) {
            gestorSesion.guardarSesion(correo);

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            // obtenemos el rol
            String rol = gestorBD.obtenerRol(correo);
            Intent intent;
            if ("admin".equals(rol)) {
                intent = new Intent(this, admin_pantallaPrincipal.class);
            } else {
                intent = new Intent(this, cliente_PantallaPrincipal.class);
            }
            intent.putExtra("CORREO_USUARIO", correo);
            startActivity(intent);
            finish();

        } else {
            // mensaje de error si no se registra
            Toast.makeText(this, "Error al registrar. El correo ya está en uso.", Toast.LENGTH_SHORT).show();
        }
    }
}
