package com.example.n_proyect.Cliente;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.R;

public class cliente_compras extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompraAdapter adapter;
    private GestorN_Proyect gestorBD;
    private String correoUsuario;
    private TextView tvSinCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_mis_compras);

        // inicializar base de datos y vistas
        gestorBD = new GestorN_Proyect(this);
        tvSinCompras = findViewById(R.id.tv_sin_compras);
        recyclerView = findViewById(R.id.rv_compras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // obtener correo del usuario en sesión
        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();

        // cargar compras del usuario
        cargarCompras();
    }

    private void cargarCompras() {
        int userId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);

        Cursor cursor = gestorBD.obtenerComprasPorUsuario(userId);

        // si hay compras, mostrar el recycler
        if (cursor != null && cursor.getCount() > 0) {
            tvSinCompras.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new CompraAdapter(cursor);
            recyclerView.setAdapter(adapter);
        } else {
            // si no hay compras, mostrar mensaje
            tvSinCompras.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    // adaptador para mostrar las compras en el recycler
    private class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.ViewHolder> {

        private final Cursor cursor;

        CompraAdapter(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflar layout de cada item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cliente_item_compra, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // mover cursor a la posición
            cursor.moveToPosition(position);

            // obtener datos del pedido
            int idPedido = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_pedido"));
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));

            // asignar datos a las vistas
            holder.tvNumeroPedido.setText("Pedido #" + idPedido);
            holder.tvFecha.setText("Fecha: " + fecha);
            holder.tvTotal.setText(String.format("Total: €%.2f", total));
            holder.txtEstado.setText(estado.toUpperCase());

            // asignar color al estado del pedido
            int colorRes = R.color.bg_estado_pendiente;
            switch (estado.toLowerCase()) {
                case "enviado":
                    colorRes = R.color.bg_estado_enviado;
                    break;
                case "entregado":
                    colorRes = R.color.bg_estado_entregado;
                    break;
                case "cancelado":
                    colorRes = R.color.bg_estado_cancelado;
                    break;
            }

            // aplicar color al chip de estado
            holder.txtEstado.setBackgroundTintList(
                    ContextCompat.getColorStateList(cliente_compras.this, colorRes)
            );

            // botón para ver detalles del pedido
            holder.btnDetalles.setOnClickListener(v -> {
                Intent intent = new Intent(cliente_compras.this, cliente_productosComprados.class);
                intent.putExtra("ID_PEDIDO", idPedido);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return cursor.getCount(); // total de elementos
        }

        // viewholder para cada item del recycler
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumeroPedido, tvFecha, tvTotal, txtEstado;
            View btnDetalles;

            ViewHolder(View itemView) {
                super(itemView);
                tvNumeroPedido = itemView.findViewById(R.id.tv_numero_pedido);
                tvFecha = itemView.findViewById(R.id.tv_fecha);
                tvTotal = itemView.findViewById(R.id.tv_total);
                txtEstado = itemView.findViewById(R.id.chip_estado);
                btnDetalles = itemView.findViewById(R.id.btn_detalles);
            }
        }
    }
}
