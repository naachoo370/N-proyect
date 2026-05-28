package com.example.n_proyect.Cliente;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.R;
import com.example.n_proyect.inicio.tienda_pantallaPrincipal;

public class cliente_usuario extends AppCompatActivity {

    private GestorN_Proyect gestorBD;
    private String correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_usuario);

        gestorBD = new GestorN_Proyect(this);

        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();

        if (correoUsuario == null) {
            Toast.makeText(this, "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvCorreo = findViewById(R.id.tv_correo);
        LinearLayout btnMisCompras = findViewById(R.id.btn_mis_compras);
        LinearLayout btnDatosPersonales = findViewById(R.id.btn_datos_personales);
        LinearLayout btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);

        cargarDatosUsuario(correoUsuario, tvCorreo);

        btnMisCompras.setOnClickListener(v -> irAMisCompras());
        btnDatosPersonales.setOnClickListener(v -> irADatosPersonales());
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

        configurarBarraNavegacion();
    }

    private void cargarDatosUsuario(String correo, TextView tvCorreo) {
        try (Cursor cursor = gestorBD.obtenerUsuarioPorCorreo(correo)) {
            if (cursor != null && cursor.moveToFirst()) {
                String correoUsuario = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                tvCorreo.setText(correoUsuario);
            } else {
                Toast.makeText(this, "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void irAMisCompras() {
        Intent intent = new Intent(this, cliente_compras.class);
        startActivity(intent);
    }

    // muestra los datos personales en un cuadro
    private void irADatosPersonales() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Datos Personales");

        View view = getLayoutInflater().inflate(R.layout.cliente_cuadro_editar_perfil_usuario, null);
        builder.setView(view);

        TextView tvCorreo = view.findViewById(R.id.tv_correo);
        TextView tvTelefono = view.findViewById(R.id.tv_telefono);
        TextView tvDireccion = view.findViewById(R.id.tv_direccion);
        Button btnActualizarEmail = view.findViewById(R.id.btn_actualizar_email);
        Button btnActualizarTelefono = view.findViewById(R.id.btn_actualizar_telefono);
        Button btnEliminarCuenta = view.findViewById(R.id.btn_eliminar_cuenta);

        try (Cursor cursor = gestorBD.obtenerUsuarioPorCorreo(correoUsuario)) {
            if (cursor != null && cursor.moveToFirst()) {
                String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
                String direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"));

                tvCorreo.setText("Correo: " + (correo != null ? correo : "Sin correo"));
                tvTelefono.setText("Teléfono: " + (telefono != null ? telefono : "Sin teléfono"));
                tvDireccion.setText("Dirección: " + (direccion != null ? direccion : "Sin dirección"));
            }
        }

        btnActualizarEmail.setOnClickListener(v -> mostrarDialogoActualizarEmailYContraseña());
        btnActualizarTelefono.setOnClickListener(v -> mostrarDialogoTelefonoyDireccion());
        btnEliminarCuenta.setOnClickListener(v -> confirmarEliminarCuenta());

        builder.create().show();
    }

    // cuadro para actualizar correo y contraseña
    private void mostrarDialogoActualizarEmailYContraseña() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Email y Contraseña");

        View view = getLayoutInflater().inflate(R.layout.cliente_cuadro_usuario_cambiar_gmail, null);
        EditText etNuevoCorreo = view.findViewById(R.id.et_nuevo_correo);
        EditText etNuevaContrasena = view.findViewById(R.id.et_nueva_contrasena);

        etNuevoCorreo.setText(correoUsuario);

        builder.setView(view);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String nuevoCorreo = etNuevoCorreo.getText().toString().trim();
            String nuevaContraseña = etNuevaContrasena.getText().toString().trim();

            // validaciones
            if (nuevoCorreo.isEmpty()) {
                Toast.makeText(this, "Debe ingresar un correo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(nuevoCorreo).matches()) {
                etNuevoCorreo.setError("Correo electrónico no válido");
                return;
            }

            if (!nuevoCorreo.equals(correoUsuario) && gestorBD.existeCorreo(nuevoCorreo)) {
                etNuevoCorreo.setError("Este correo ya está registrado");
                return;
            }

            if (nuevaContraseña.isEmpty()) {
                Toast.makeText(this, "Debe ingresar una nueva contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // actualizar en la bd
            int idUsuario = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);

            boolean correoActualizado = gestorBD.actualizarCorreoUsuario(idUsuario, nuevoCorreo);
            boolean contrasenaActualizada = gestorBD.actualizarContraseñaUsuario(idUsuario, nuevaContraseña);

            if (correoActualizado || contrasenaActualizada) {
                correoUsuario = nuevoCorreo;

                // actualizar sesión
                ControlSesion sesionManager = new ControlSesion(this);
                sesionManager.guardarSesion(nuevoCorreo);

                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    // cuadro para actualizar telefono y dirección
    private void mostrarDialogoTelefonoyDireccion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Datos de Contacto");

        View view = getLayoutInflater().inflate(R.layout.cliente_cuadro_usuario_cambiar_telefono, null);
        EditText etNuevoTelefono = view.findViewById(R.id.et_nuevo_telefono);
        EditText etNuevaDireccion = view.findViewById(R.id.et_nueva_direccion);

        // cargar datos actuales
        try (Cursor cursor = gestorBD.obtenerUsuarioPorCorreo(correoUsuario)) {
            if (cursor != null && cursor.moveToFirst()) {
                etNuevoTelefono.setText(cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
                etNuevaDireccion.setText(cursor.getString(cursor.getColumnIndexOrThrow("direccion")));
            }
        }

        builder.setView(view);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String nuevoTelefono = etNuevoTelefono.getText().toString().trim();
            String nuevaDireccion = etNuevaDireccion.getText().toString().trim();

            int idUsuario = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);

            boolean telefonoActualizado = gestorBD.actualizarTelefonoUsuario(idUsuario, nuevoTelefono);
            boolean direccionActualizada = gestorBD.actualizarDireccionUsuario(idUsuario, nuevaDireccion);

            if (telefonoActualizado || direccionActualizada) {
                Toast.makeText(this, "Datos de contacto actualizados", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se realizaron cambios", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    // eliminacuenta
    private void confirmarEliminarCuenta() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar cuenta")
                .setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    int idUsuario = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);
                    if (gestorBD.eliminarUsuario(idUsuario)) {
                        Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        cerrarSesion();
                    } else {
                        Toast.makeText(this, "Error al eliminar cuenta", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // cierra sesion y va a inicio
    private void cerrarSesion() {
        ControlSesion sesionManager = new ControlSesion(this);
        sesionManager.cerrarSesion();

        Intent intent = new Intent(this, tienda_pantallaPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // barra navegacion
    private void configurarBarraNavegacion() {
        LinearLayout navInicio = findViewById(R.id.nav_inicio);
        LinearLayout navMenu = findViewById(R.id.nav_menu);
        LinearLayout navCarrito = findViewById(R.id.nav_carrito);
        LinearLayout navCuenta = findViewById(R.id.nav_cuenta);

        navInicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_novedades.class);
            startActivity(intent);
        });

        navMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_PantallaPrincipal.class);
            startActivity(intent);
        });

        navCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_carrito.class);
            startActivity(intent);
        });

        navCuenta.setOnClickListener(v -> {
            if (this instanceof cliente_usuario) {
                Toast.makeText(this, "Ya estas en usuario", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, cliente_PantallaPrincipal.class));
            }
        });
    }
}
