package com.example.n_proyect.Cliente;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.R;

public class cliente_productosComprados extends AppCompatActivity {

    private RecyclerView recyclerView;
    private cliente_mostraProductosEnCarrito adapter;
    private GestorN_Proyect gestorBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_detalle_compra); // cargar xml

        recyclerView = findViewById(R.id.rv_detalles);
        gestorBD = new GestorN_Proyect(this);

        int idPedido = getIntent().getIntExtra("ID_PEDIDO", -1); // obtiene el id del pedido

        configurarRecyclerView();

        if(idPedido != -1) {
            cargarDetalles(idPedido); // carga productos si el id es valido
        } else {
            Toast.makeText(this, "Error al cargar los detalles", Toast.LENGTH_SHORT).show();
            finish(); // cierra si no hay id válido
        }
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new cliente_mostraProductosEnCarrito(this, null);
        recyclerView.setAdapter(adapter);
    }

    private void cargarDetalles(int idCompra) {
        Cursor cursor = gestorBD.obtenerDetallesCompra(idCompra); // consulta productos
        adapter.swapCursor(cursor); // muestra productos en el recycler
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.swapCursor(null);
        }
    }
}
