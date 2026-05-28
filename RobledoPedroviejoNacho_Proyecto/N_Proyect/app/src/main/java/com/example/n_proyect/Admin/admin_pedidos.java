package com.example.n_proyect.Admin;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.Pedido;
import com.example.n_proyect.R;
import java.util.ArrayList;
import java.util.List;

public class admin_pedidos extends AppCompatActivity implements admin_pedidosAdaptador.OnPedidoAdminListener {

    private RecyclerView rvPedidos;
    private admin_pedidosAdaptador adapter;
    private GestorN_Proyect gestorBD;
    private List<Pedido> listaPedidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_pedidos);

        gestorBD = new GestorN_Proyect(this);
        configurarVistas();
        cargarPedidos();
    }

    private void configurarVistas() {
        rvPedidos = findViewById(R.id.rvPedidos);
        rvPedidos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new admin_pedidosAdaptador(listaPedidos, this);
        rvPedidos.setAdapter(adapter);

        // configurar barra de búsqueda
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String nuevoTexto) {
                filtrarPedidos(nuevoTexto); // filtrar al escribir
                return true;
            }
        });
    }

    // cargar todos los pedidos de la bd
    private void cargarPedidos() {
        listaPedidos.clear();
        listaPedidos.addAll(gestorBD.obtenerTodosPedidos());
        adapter.notifyDataSetChanged();
    }

    private void filtrarPedidos(String texto) {
        List<Pedido> filtrados = new ArrayList<>();
        for (Pedido p : listaPedidos) {
            if (p.getCorreoCliente().toLowerCase().contains(texto.toLowerCase()) ||
                    p.getFechaPedido().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(p);
            }
        }
        adapter.actualizarLista(filtrados);
    }


    @Override
    public void onEditarClick(int position) {
        Pedido pedido = listaPedidos.get(position);
        admin_editarPedido dialog = admin_editarPedido.newInstance(pedido);
        dialog.show(getSupportFragmentManager(), "EditarPedidoDialog");
    }

    // eliminar pedido
    @Override
    public void onEliminarClick(int position) {
        Pedido pedido = listaPedidos.get(position);
        if (gestorBD.eliminarPedido(pedido.getId())) {
            gestorBD.restaurarStock(pedido.getId()); // restaurar stock
            listaPedidos.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }


    public void actualizarPedido(Pedido pedidoActualizado) {
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (listaPedidos.get(i).getId() == pedidoActualizado.getId()) {
                listaPedidos.set(i, pedidoActualizado);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    public void eliminarPedidoDeLista(int pedidoId) {
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (listaPedidos.get(i).getId() == pedidoId) {
                listaPedidos.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }
}
