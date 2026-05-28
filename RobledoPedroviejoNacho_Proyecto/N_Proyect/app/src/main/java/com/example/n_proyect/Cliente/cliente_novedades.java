package com.example.n_proyect.Cliente;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.ProductoVideo;
import com.example.n_proyect.R;

import java.util.List;

public class cliente_novedades extends AppCompatActivity {

    private RecyclerView rvNovedades;
    private GestorN_Proyect gestor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_novedades);

        gestor = new GestorN_Proyect(this);
        configurarVistas();
        cargarNovedades();
        configurarNavegacion();
    }

    private void configurarVistas() {
        rvNovedades = findViewById(R.id.rvNovedades);
        rvNovedades.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void cargarNovedades() {
        List<ProductoVideo> novedades = gestor.obtenerVideosProductos();
        cliente_novedadesAdaptador adapter = new cliente_novedadesAdaptador(this, novedades);
        rvNovedades.setAdapter(adapter);
    }

    private void configurarNavegacion() {
        // boton inicio
        findViewById(R.id.nav_inicio).setOnClickListener(v ->{
            if (this instanceof cliente_novedades) {
                Toast.makeText(this, "Ya estas en Incio", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, cliente_PantallaPrincipal.class));
            }
        });

        // boton menu
        findViewById(R.id.nav_menu).setOnClickListener(v ->
                startActivity(new Intent(this, cliente_PantallaPrincipal.class)));

        // boton carrito
        findViewById(R.id.nav_carrito).setOnClickListener(v ->
                startActivity(new Intent(this, cliente_carrito.class)));

        // boton cuenta
        findViewById(R.id.nav_cuenta).setOnClickListener(v ->
                startActivity(new Intent(this, cliente_usuario.class)));
    }
}
