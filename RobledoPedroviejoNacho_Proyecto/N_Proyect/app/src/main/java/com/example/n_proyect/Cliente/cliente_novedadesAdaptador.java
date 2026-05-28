package com.example.n_proyect.Cliente;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.Producto;
import com.example.n_proyect.Clases.ProductoVideo;
import com.example.n_proyect.R;

import java.util.List;

public class cliente_novedadesAdaptador extends RecyclerView.Adapter<cliente_novedadesAdaptador.NovedadViewHolder> {

    private final Context context;
    private final List<ProductoVideo> novedades;
    private final GestorN_Proyect gestor;

    public cliente_novedadesAdaptador(Context context, List<ProductoVideo> novedades) {
        this.context = context;
        this.novedades = novedades;
        this.gestor = new GestorN_Proyect(context);
    }

    @NonNull
    @Override
    public NovedadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // mete el layout del item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cliente_item_novedad, parent, false);
        return new NovedadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovedadViewHolder holder, int position) {
        // saca la novedad y el producto asociado
        ProductoVideo novedad = novedades.get(position);
        Producto producto = gestor.obtenerProductoPorId(novedad.getProductoId());

        if (producto != null) {
            holder.txtTitulo.setText(producto.getNombre());
            configurarMultimedia(holder, producto, novedad.getNombreVideo()); // video o imagen
            configurarBotonDetalles(holder, producto.getId());
        }
    }

    private void configurarMultimedia(NovedadViewHolder holder, Producto producto, String nombreVideo) {
        try {
            holder.videoView.stopPlayback();

            if (nombreVideo != null && !nombreVideo.isEmpty()) {
                int resId = context.getResources().getIdentifier(
                        nombreVideo.split("\\.")[0],
                        "raw",
                        context.getPackageName()
                );

                if (resId != 0) {
                    // pone el video
                    Uri videoUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
                    holder.videoView.setVideoURI(videoUri);
                    holder.videoView.setOnPreparedListener(mp -> {
                        mp.setLooping(true);
                        mp.start();
                    });
                    holder.videoView.setVisibility(View.VISIBLE);
                    holder.imgProducto.setVisibility(View.GONE);
                    return;
                }
            }
            mostrarImagenRespaldo(holder, producto); // si no hay video
        } catch (Exception e) {
            mostrarImagenRespaldo(holder, producto); // si hay error
        }
    }

    private void mostrarImagenRespaldo(NovedadViewHolder holder, Producto producto) {
        try {
            // saca la imagen del drawable
            int resId = context.getResources().getIdentifier(
                    producto.getImagenPrincipal().split("\\.")[0],
                    "drawable",
                    context.getPackageName()
            );
            holder.imgProducto.setImageResource(resId);
            holder.imgProducto.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
        } catch (Exception e) {
            holder.imgProducto.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
        }
    }

    private void configurarBotonDetalles(NovedadViewHolder holder, int productoId) {
        // abre detalles del producto
        holder.btnDetalles.setOnClickListener(v -> {
            Intent intent = new Intent(context, cliente_datosProducto.class);
            intent.putExtra("PRODUCTO_ID", productoId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return novedades.size();
    }

    @Override
    public void onViewRecycled(@NonNull NovedadViewHolder holder) {
        super.onViewRecycled(holder);
        holder.videoView.stopPlayback();
        holder.videoView.setVisibility(View.GONE);
        holder.imgProducto.setVisibility(View.VISIBLE);
    }

    static class NovedadViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageView imgProducto;
        TextView txtTitulo;
        Button btnDetalles;

        public NovedadViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            btnDetalles = itemView.findViewById(R.id.btnDetalles);
        }
    }
}
