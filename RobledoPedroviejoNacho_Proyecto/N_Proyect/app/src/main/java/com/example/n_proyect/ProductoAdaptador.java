package com.example.n_proyect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n_proyect.Clases.Producto;

import java.util.List;

public class ProductoAdaptador extends RecyclerView.Adapter<ProductoAdaptador.ProductoViewHolder> {

    private Context context;
    private List<Producto> listaProductos;
    private OnProductoClickListener listener;
    private int placeholderResId;

    public interface OnProductoClickListener {
        void onProductoClick(int productoId);
    }

    public ProductoAdaptador(Context context, List<Producto> listaProductos, OnProductoClickListener listener) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.listener = listener;
        this.placeholderResId = R.drawable.placeholder_camiseta;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cliente_item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.txtNombre.setText(producto.getNombre());
        holder.txtDescripcion.setText(producto.getDescripcion());
        holder.txtPrecio.setText(String.format("€%.2f", producto.getPrecio()));

        String nombreImagen = producto.getImagenPrincipal().replace(".png", "");
        int resId = context.getResources().getIdentifier(
                nombreImagen,
                "drawable",
                context.getPackageName()
        );

        holder.imgProducto.setImageResource(resId != 0 ? resId : placeholderResId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductoClick(producto.getId());
            }
        });
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        this.listaProductos = nuevaLista;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaProductos != null ? listaProductos.size() : 0;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtDescripcion, txtPrecio;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.img_producto);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtDescripcion = itemView.findViewById(R.id.txt_descripcion);
            txtPrecio = itemView.findViewById(R.id.txt_precio);
        }
    }
}