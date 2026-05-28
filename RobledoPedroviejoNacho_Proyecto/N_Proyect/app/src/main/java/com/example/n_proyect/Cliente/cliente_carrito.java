package com.example.n_proyect.Cliente;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.Carrito;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.R;

import java.util.ArrayList;
import java.util.List;

public class cliente_carrito extends AppCompatActivity {

    // variables xml
    private RecyclerView recyclerView;
    private cliente_carritoAdaptador adapter;
    private GestorN_Proyect gestorBD;
    private TextView txtTotal;
    private Button btnPagar;
    private String correoUsuario;
    private double totalCarrito;
    LinearLayout navInicio, navMenu, navCarrito, navCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_carrito); // cargar XML

        // obtener correo de sesion
        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();

        if (correoUsuario == null) {
            finish(); // si no hay sesion se cieera
            return;
        }

        // Enlazar con XML
        gestorBD = new GestorN_Proyect(this);
        recyclerView = findViewById(R.id.recycler_carrito);
        txtTotal = findViewById(R.id.txt_total);
        btnPagar = findViewById(R.id.btn_pagar);
        navInicio = findViewById(R.id.nav_inicio);
        navMenu = findViewById(R.id.nav_menu);
        navCarrito = findViewById(R.id.nav_carrito);
        navCuenta = findViewById(R.id.nav_cuenta);

        configurarBarraInferior(); // barra inferior
        configurarRecyclerView();  // recyclerView
        cargarDatosCarrito();      // cargar datos del carrito
        configurarBotonPago();     // btn pagar
    }

    // configurar lista
    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new cliente_carritoAdaptador(
                this,
                new ArrayList<>(),
                position -> {
                    Carrito item = adapter.getItem(position);
                    gestorBD.eliminarDelCarrito(item.getId());
                    actualizarCarritoDespuesDeEliminar();
                }
        );

        recyclerView.setAdapter(adapter);
    }

    // cargar carrito desde BD
    private void cargarDatosCarrito() {
        int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);
        List<Carrito> items = gestorBD.obtenerCarrito(usuarioId);
        adapter.setItems(items);
        calcularTotal(items); // precio total del carrito
    }


    private void actualizarCarritoDespuesDeEliminar() {
        int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);
        List<Carrito> itemsActualizados = gestorBD.obtenerCarrito(usuarioId);
        adapter.setItems(itemsActualizados);
        calcularTotal(itemsActualizados);
    }

    // calcular total del carrito
    private void calcularTotal(List<Carrito> items) {
        totalCarrito = 0;
        for (Carrito item : items) {
            totalCarrito += item.getPrecioUnitario() * item.getCantidad();
        }
        txtTotal.setText(String.format("€%.2f", totalCarrito));
        btnPagar.setEnabled(totalCarrito > 0); // solo si hay productos
    }

    // bton pagar
    private void configurarBotonPago() {
        btnPagar.setOnClickListener(v -> {
            if (totalCarrito > 0) {
                Intent intent = new Intent(cliente_carrito.this, cliente_pago.class);
                intent.putExtra("TOTAL_PEDIDO", totalCarrito);
                startActivity(intent);
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // al volver
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosCarrito();
    }

    // navegación inferior
    private void configurarBarraInferior() {
        navInicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_novedades.class);
            startActivity(intent);
        });

        navMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, cliente_PantallaPrincipal.class));
        });

        navCarrito.setOnClickListener(v -> {
            if (this instanceof cliente_carrito) {
                Toast.makeText(this, "Ya estás en carrito", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, cliente_PantallaPrincipal.class));
            }
        });

        navCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_usuario.class);
            startActivity(intent);
        });
    }

}
