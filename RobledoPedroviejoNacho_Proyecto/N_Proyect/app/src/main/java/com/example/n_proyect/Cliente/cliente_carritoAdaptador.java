package com.example.n_proyect.Cliente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n_proyect.Clases.Carrito;
import com.example.n_proyect.R;

import java.util.ArrayList;
import java.util.List;

public class cliente_carritoAdaptador extends RecyclerView.Adapter<cliente_carritoAdaptador.CarritoViewHolder> {

    // variables
    private List<Carrito> items;
    private OnItemClickListener listener;
    private Context context;

    // manejo de elimniar
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    // constructor
    public cliente_carritoAdaptador(Context context, List<Carrito> items, OnItemClickListener listener) {
        this.context = context;
        this.items = new ArrayList<>(items);
        this.listener = listener;
    }

    // obtener item por posicion
    public Carrito getItem(int position) {
        return items.get(position);
    }

    // actualizar items del adaptador
    public void setItems(List<Carrito> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    // cargar layout del item
    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cliente_item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    // vincular datos al viewholder
    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Carrito item = items.get(position);

        // cargar imagen del producto
        int resId = context.getResources().getIdentifier(
                item.getImagenProducto().replace(".png", ""),
                "drawable",
                context.getPackageName()
        );
        holder.imgProducto.setImageResource(resId);

        // asignar textos
        holder.txtNombre.setText(item.getNombreProducto());
        holder.txtTalla.setText("Talla: " + item.getTalla());
        holder.txtCantidad.setText(String.valueOf(item.getCantidad()));
        holder.txtPrecio.setText(String.format("€%.2f", item.getPrecioUnitario()));

        // eliminar producto
        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(position);
            }
        });
    }

    // cantidad de items
    @Override
    public int getItemCount() {
        return items.size();
    }

    // clase viewholder
    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtTalla, txtCantidad, txtPrecio;
        ImageView btnEliminar;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            // enlazar con xml
            imgProducto = itemView.findViewById(R.id.img_producto_carrito);
            txtNombre = itemView.findViewById(R.id.txt_nombre_producto);
            txtTalla = itemView.findViewById(R.id.txt_talla);
            txtCantidad = itemView.findViewById(R.id.txt_cantidad);
            txtPrecio = itemView.findViewById(R.id.txt_precio);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}
