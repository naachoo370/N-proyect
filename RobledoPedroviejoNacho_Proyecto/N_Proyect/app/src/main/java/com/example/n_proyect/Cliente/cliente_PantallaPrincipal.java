package com.example.n_proyect.Cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.ProductosHombre.ProductosAbrigosHombre;
import com.example.n_proyect.ProductosMujer.ProductosAbrigosMujer;
import com.example.n_proyect.ProductosHombre.ProductosCamisetasHombre;
import com.example.n_proyect.ProductosMujer.ProductosCamisetasMujer;
import com.example.n_proyect.ProductosHombre.ProductosPantalonesHombre;
import com.example.n_proyect.ProductosMujer.ProductosPantalonesMujer;
import com.example.n_proyect.ProductosHombre.ProductosPolosHombre;
import com.example.n_proyect.ProductosMujer.ProductosPuntoMujer;
import com.example.n_proyect.ProductosHombre.ProductosSudaderaHombre;
import com.example.n_proyect.ProductosMujer.ProductosSudaderaMujer;
import com.example.n_proyect.R;

public class cliente_PantallaPrincipal extends AppCompatActivity {

    TextView tabMujer, tabHombre;

    ScrollView vistaMujer, vistaHombre;

    LinearLayout mujerNovedades, mujerCamisetas, mujerPunto, mujerSudaderas, mujerChaquetas, mujerPantalones;

    LinearLayout hombreNovedades, hombreCamisetas, hombreSudaderas, hombrePolos, hombrePantalones, hombreAbrigos;

    LinearLayout navInicio, navMenu, navCarrito, navCuenta;

    private String correoUsuario;
    private ControlSesion sesionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_menu); // carga xml

        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();

        if (correoUsuario == null) {
            Toast.makeText(this, "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tabMujer = findViewById(R.id.tab_mujer);
        tabHombre = findViewById(R.id.tab_hombre);

        vistaMujer = findViewById(R.id.vista_mujer);
        vistaHombre = findViewById(R.id.vista_hombre);

        mujerCamisetas = findViewById(R.id.mujer_camisetas);
        mujerPunto = findViewById(R.id.mujer_punto);
        mujerSudaderas = findViewById(R.id.mujer_sudaderas);
        mujerChaquetas = findViewById(R.id.mujer_chaquetas);
        mujerPantalones = findViewById(R.id.mujer_pantalones);

        hombreCamisetas = findViewById(R.id.hombre_camisetas);
        hombreSudaderas = findViewById(R.id.hombre_sudaderas);
        hombrePolos = findViewById(R.id.hombre_polos);
        hombrePantalones = findViewById(R.id.hombre_pantalones);
        hombreAbrigos = findViewById(R.id.hombre_abrigos);

        navInicio = findViewById(R.id.nav_inicio);
        navMenu = findViewById(R.id.nav_menu);
        navCarrito = findViewById(R.id.nav_carrito);
        navCuenta = findViewById(R.id.nav_cuenta);

        // por defecto mostrar mujer y ocultar hombre
        vistaMujer.setVisibility(View.VISIBLE);
        vistaHombre.setVisibility(View.GONE);
        tabMujer.setBackgroundColor(getResources().getColor(R.color.white));
        tabHombre.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        configurarTabsGenero();
        configurarCategorias();
        configurarBarraInferior();
    }

    // cambia entre categorias 
    private void configurarTabsGenero() {
        // estado inicial
        tabMujer.setBackgroundColor(getResources().getColor(R.color.white));
        tabMujer.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabHombre.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabHombre.setTextColor(getResources().getColor(R.color.white));

        tabMujer.setOnClickListener(v -> {
            vistaMujer.setVisibility(View.VISIBLE);
            vistaHombre.setVisibility(View.GONE);
            tabMujer.setBackgroundColor(getResources().getColor(R.color.white));
            tabMujer.setTextColor(getResources().getColor(R.color.colorPrimary));
            tabHombre.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            tabHombre.setTextColor(getResources().getColor(R.color.white));
        });

        tabHombre.setOnClickListener(v -> {
            vistaMujer.setVisibility(View.GONE);
            vistaHombre.setVisibility(View.VISIBLE);
            tabHombre.setBackgroundColor(getResources().getColor(R.color.white));
            tabHombre.setTextColor(getResources().getColor(R.color.colorPrimary));
            tabMujer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            tabMujer.setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void configurarCategorias() {
        // mujer
        mujerCamisetas.setOnClickListener(v -> mostrarCategoria("Camisetas Mujer"));
        mujerPunto.setOnClickListener(v -> mostrarCategoria("Punto Mujer"));
        mujerSudaderas.setOnClickListener(v -> mostrarCategoria("Sudaderas Mujer"));
        mujerChaquetas.setOnClickListener(v -> mostrarCategoria("Chaquetas Mujer"));
        mujerPantalones.setOnClickListener(v -> mostrarCategoria("Pantalones Mujer"));

        // hombre
        hombreCamisetas.setOnClickListener(v -> mostrarCategoria("Camisetas Hombre"));
        hombreSudaderas.setOnClickListener(v -> mostrarCategoria("Sudaderas Hombre"));
        hombrePolos.setOnClickListener(v -> mostrarCategoria("Polos Hombre"));
        hombrePantalones.setOnClickListener(v -> mostrarCategoria("Pantalones Hombre"));
        hombreAbrigos.setOnClickListener(v -> mostrarCategoria("Abrigos Hombre"));
    }

    private void mostrarCategoria(String nombreCategoria) {
        Intent intent;
        switch (nombreCategoria) {
            case "Camisetas Hombre":
                intent = new Intent(this, ProductosCamisetasHombre.class);
                break;
            case "Sudaderas Hombre":
                intent = new Intent(this, ProductosSudaderaHombre.class);
                break;
            case "Pantalones Hombre":
                intent = new Intent(this, ProductosPantalonesHombre.class);
                break;
            case "Abrigos Hombre":
                intent = new Intent(this, ProductosAbrigosHombre.class);
                break;
            case "Polos Hombre":
                intent = new Intent(this, ProductosPolosHombre.class);
                break;
            case "Sudaderas Mujer":
                intent = new Intent(this, ProductosSudaderaMujer.class);
                break;
            case "Punto Mujer":
                intent = new Intent(this, ProductosPuntoMujer.class);
                break;
            case "Pantalones Mujer":
                intent = new Intent(this, ProductosPantalonesMujer.class);
                break;
            case "Camisetas Mujer":
                intent = new Intent(this, ProductosCamisetasMujer.class);
                break;
            case "Chaquetas Mujer":
                intent = new Intent(this, ProductosAbrigosMujer.class);
                break;
            default:
                Toast.makeText(this, "Categoría: " + nombreCategoria, Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
    }

    // barra de navegacion 
    private void configurarBarraInferior() {
        navInicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_novedades.class);
            startActivity(intent);
        });

        navMenu.setOnClickListener(v -> {
            if (this instanceof cliente_PantallaPrincipal) {
                Toast.makeText(this, "Ya estás en el menú", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, cliente_PantallaPrincipal.class));
            }
        });

        navCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_carrito.class);
            startActivity(intent);
        });

        navCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(this, cliente_usuario.class);
            startActivity(intent);
        });
    }
}
