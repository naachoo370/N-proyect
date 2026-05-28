package com.example.n_proyect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductoImagen extends RecyclerView.Adapter<ProductoImagen.ImagenViewHolder> {

    private List<String> imagenes;

    public ProductoImagen(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    @NonNull
    @Override
    public ImagenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cliente_item_imagen_producto, parent, false);
        return new ImagenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenViewHolder holder, int position) {
        String nombreImagen = imagenes.get(position).replace(".png", "");
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                nombreImagen,
                "drawable",
                holder.itemView.getContext().getPackageName()
        );
        holder.imageView.setImageResource(resId);
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    static class ImagenViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImagenViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_carrusel);
        }
    }
}