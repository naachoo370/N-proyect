package com.example.n_proyect.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n_proyect.Clases.Pedido;
import com.example.n_proyect.R;

import java.util.List;

public class admin_pedidosAdaptador extends RecyclerView.Adapter<admin_pedidosAdaptador.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private final OnPedidoAdminListener listener;

    // interfaz para manejar clics en editar y eliminar
    public interface OnPedidoAdminListener {
        void onEditarClick(int position);
        void onEliminarClick(int position);
    }

    // constructor recibe lista de pedidos y listener
    public admin_pedidosAdaptador(List<Pedido> pedidos, OnPedidoAdminListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    // mete el layout del item de pedido
    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    // asigna datos del pedido a los views
    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.tvPedidoId.setText("Pedido #" + pedido.getId());
        holder.tvFecha.setText(pedido.getFechaPedido());
        holder.tvCliente.setText(pedido.getCorreoCliente());
        holder.tvTotal.setText(String.format("Total: €%.2f", pedido.getTotal()));
        holder.tvEstado.setText(pedido.getEstado());

        // estado del pedido
        switch (pedido.getEstado().toLowerCase()) {
            case "pendiente":
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente);
                break;
            case "enviado":
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_enviado);
                break;
            case "entregado":
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_entregado);
                break;
            case "cancelado":
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_cancelado);
                break;
        }

        // bton para editar y eliminar
        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(position));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(position));
    }

    // devuelve cantidad de pedidos en la lista
    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    // actualiza la lista del adaptador
    public void actualizarLista(List<Pedido> nuevaLista) {
        pedidos = nuevaLista;
        notifyDataSetChanged();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvPedidoId, tvFecha, tvCliente, tvTotal, tvEstado;
        Button btnEditar, btnEliminar;

        PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPedidoId = itemView.findViewById(R.id.tvPedidoId);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
