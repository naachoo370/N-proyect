package com.example.n_proyect.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.R;
import com.example.n_proyect.inicio.tienda_inicioSesion;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class admin_pantallaPrincipal extends AppCompatActivity {

    private TextView textoPanelAdministracion;
    private Button btnPedidos, btnStock, btnProductos, btnUsuarios;
    private TextView tvMujerTotal, tvHombreTotal, tvpedidoshoy;

    // hilo para tareas en segundo plano
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    // handler para actualizar vista desde hilo principal
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda_admin);

        textoPanelAdministracion = findViewById(R.id.textoPanelAdministracion);
        btnPedidos = findViewById(R.id.btnPedidos);
        btnStock = findViewById(R.id.btnStock);
        btnProductos = findViewById(R.id.btnProductos);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        tvMujerTotal = findViewById(R.id.tvMujerTotal);
        tvHombreTotal = findViewById(R.id.tvHombreTotal);
        tvpedidoshoy = findViewById(R.id.tvpedidoshoy);

        configurarEventos();
        cargarEstadisticas();
    }


    private void configurarEventos() {
        btnPedidos.setOnClickListener(view -> abrirPedidos());
        btnStock.setOnClickListener(view -> abrirStock());
        btnProductos.setOnClickListener(view -> abrirProductos());
        btnUsuarios.setOnClickListener(view -> abrirUsuarios());
    }

    private void abrirPedidos() {
        startActivity(new Intent(this, admin_pedidos.class));
    }

    private void abrirStock() {
        startActivity(new Intent(this, admin_stock.class));
    }

    private void abrirProductos() {
        startActivity(new Intent(this, admin_productos.class));
    }

    private void abrirUsuarios() {
        startActivity(new Intent(this, admin_usuarios.class));
    }

    // cargar estadísticas desde base de datos
    private void cargarEstadisticas() {
        executorService.execute(() -> {
            GestorN_Proyect gestor = new GestorN_Proyect(admin_pantallaPrincipal.this);
            int[] resultados = new int[]{
                    gestor.obtenerTotalProductosMujer(),
                    gestor.obtenerTotalProductosHombre(),
                    gestor.obtenerPedidosHoy()
            };

            // actualizar texto
            mainHandler.post(() -> {
                tvMujerTotal.setText(String.valueOf(resultados[0]));
                tvHombreTotal.setText(String.valueOf(resultados[1]));
                tvpedidoshoy.setText(String.valueOf(resultados[2]));
            });
        });
    }

    // al pulsar atras, cerrar sesión
    @Override
    public void onBackPressed() {
        cerrarSesionYSalir();
    }


    private void cerrarSesionYSalir() {
        ControlSesion controlSesion = new ControlSesion(this);
        controlSesion.cerrarSesion();

        Intent intent = new Intent(this, tienda_inicioSesion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // quitar los hilos
    }
}
