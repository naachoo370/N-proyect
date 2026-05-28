package com.example.n_proyect.ProductosMujer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Cliente.cliente_carrito;
import com.example.n_proyect.Cliente.cliente_PantallaPrincipal;
import com.example.n_proyect.Cliente.cliente_datosProducto;
import com.example.n_proyect.Cliente.cliente_usuario;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.Clases.Producto;
import com.example.n_proyect.ProductoAdaptador;
import com.example.n_proyect.ProductosHombre.ProductosSudaderaHombre;
import com.example.n_proyect.R;

import java.util.List;

public class ProductosSudaderaMujer extends AppCompatActivity implements ProductoAdaptador.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductoAdaptador adapter;
    private String correoUsuario;
    private SearchView searchView;
    private Spinner filtroTallas;
    private GestorN_Proyect gestorBD;

    private View navInicio, navMenu, navCarrito, navCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_categoria_sudadera_mujer);

        searchView = findViewById(R.id.search_view);
        filtroTallas = findViewById(R.id.filtro_tallas);
        recyclerView = findViewById(R.id.recycler_productos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        navInicio = findViewById(R.id.nav_inicio);
        navMenu = findViewById(R.id.nav_menu);
        navCarrito = findViewById(R.id.nav_carrito);
        navCuenta = findViewById(R.id.nav_cuenta);

        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();

        if (correoUsuario == null) {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        gestorBD = new GestorN_Proyect(this);

        configurarBusqueda();
        configurarFiltroTallas();
        cargarProductosSudaderas();
        configurarBarraInferior();
    }

    private void configurarBarraInferior() {
        navInicio.setOnClickListener(v -> navigateTo(cliente_PantallaPrincipal.class));
        navMenu.setOnClickListener(v -> showCurrentLocationToast("menú"));
        navCarrito.setOnClickListener(v -> navigateTo(cliente_carrito.class));
        navCuenta.setOnClickListener(v -> navigateTo(cliente_usuario.class));
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        startActivity(intent);
        if (!destination.equals(ProductosSudaderaHombre.class)) {
            finish();
        }
    }

    private void showCurrentLocationToast(String localizacion) {
        Toast.makeText(this, "Ya estas en el " + localizacion, Toast.LENGTH_SHORT).show();
    }

    private void configurarBusqueda() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                aplicarFiltros();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aplicarFiltros();
                return true;
            }
        });
    }

    private void configurarFiltroTallas() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Todas", "XS", "S", "M", "L", "XL", "XXL"}
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtroTallas.setAdapter(spinnerAdapter);
        filtroTallas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                aplicarFiltros();
            }
        });
    }

    private void cargarProductosSudaderas() {
        List<Producto> productos = gestorBD.obtenerProductosSudaderasMujer();
        actualizarAdaptador(productos);
    }

    private void aplicarFiltros() {
        String talla = filtroTallas.getSelectedItem().toString();
        String query = searchView.getQuery().toString();

        List<Producto> productos = gestorBD.filtrarProductosPorCategoria(
                "Sudaderas", // categoria
                "mujer", // genero
                talla,
                query
        );
        actualizarAdaptador(productos);
    }

    private void actualizarAdaptador(List<Producto> productos) {
        if (adapter == null) {
            adapter = new ProductoAdaptador(this, productos, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.actualizarLista(productos);
        }
    }

    @Override
    public void onProductoClick(int productoId) {
        Intent intent = new Intent(this, cliente_datosProducto.class);
        intent.putExtra("PRODUCTO_ID", productoId);
        startActivity(intent);
    }
}
