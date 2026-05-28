package com.example.n_proyect.Admin;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.R;
import com.example.n_proyect.Clases.Usuario;

import java.util.ArrayList;
import java.util.List;

public class admin_usuarios extends AppCompatActivity {


    private RecyclerView recyclerView;
    private admin_usuariosAdaptador adaptador;
    private GestorN_Proyect gestorBD;
    private List<Usuario> listaUsuarios;
    private List<Usuario> listaUsuariosCompleta;
    private SearchView buscador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_usuarios);

        // inicializar bd y listas
        gestorBD = new GestorN_Proyect(this);
        listaUsuarios = new ArrayList<>();
        listaUsuariosCompleta = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buscador = findViewById(R.id.buscadorUsuarios);
        configurarBuscador();

        cargarUsuarios();
    }

    private void configurarBuscador() {
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // al cambiar el texto se filtran los usuarios
            @Override
            public boolean onQueryTextChange(String textoBusqueda) {
                filtrarUsuarios(textoBusqueda);
                return true;
            }
        });
    }

    private void filtrarUsuarios(String texto) {
        listaUsuarios.clear();

        if (texto.isEmpty()) {
            listaUsuarios.addAll(listaUsuariosCompleta);
        } else {
            String textoBusqueda = texto.toLowerCase().trim();
            for (Usuario usuario : listaUsuariosCompleta) {
                if (usuario.getCorreo().toLowerCase().contains(textoBusqueda)) {
                    listaUsuarios.add(usuario);
                }
            }
        }

        adaptador.notifyDataSetChanged();
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        listaUsuariosCompleta.clear();

        Cursor cursor = gestorBD.obtenerTodosUsuarios();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                String rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"));

                Usuario usuario = new Usuario(id, correo, rol);
                listaUsuarios.add(usuario);
                listaUsuariosCompleta.add(usuario);
            } while (cursor.moveToNext());
            cursor.close();
        }

        adaptador = new admin_usuariosAdaptador(this, listaUsuarios, gestorBD);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    protected void onDestroy() {
        gestorBD.close();
        super.onDestroy();
    }
}
