package com.example.n_proyect.Cliente;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.R;

public class cliente_mostraProductosEnCarrito extends RecyclerView.Adapter<cliente_mostraProductosEnCarrito.DetalleViewHolder> {

    private Cursor cursor;
    private final Context context;

    // constructor
    public cliente_mostraProductosEnCarrito(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    // meter el layout de cada item
    @NonNull
    @Override
    public DetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cliente_item_detalle_producto, parent, false);
        return new DetalleViewHolder(view);
    }

    // asigna los datos
    @Override
    public void onBindViewHolder(@NonNull DetalleViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto"));
        int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
        double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio_unitario"));
        String imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen_producto"));

        int resId = context.getResources().getIdentifier(
                imagen.replace(".png", ""),
                "drawable",
                context.getPackageName()
        );

        holder.imgProducto.setImageResource(resId);
        holder.txtNombre.setText(nombre);
        holder.txtCantidad.setText("Cantidad: " + cantidad);
        holder.txtPrecio.setText(String.format("€%.2f", precio * cantidad));
    }

    // devuelve la cantidad de elementos
    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    // actualiza el cursor con nuevos datos
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
    }

    // viewholder con las vistas del item
    static class DetalleViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtCantidad, txtPrecio;

        public DetalleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.img_producto);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtCantidad = itemView.findViewById(R.id.txt_cantidad);
            txtPrecio = itemView.findViewById(R.id.txt_precio);
        }
    }
}
