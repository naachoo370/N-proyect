package com.example.n_proyect.Admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.Producto;
import com.example.n_proyect.R;

import java.util.ArrayList;
import java.util.List;

public class admin_productos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductosAdminAdapter adapter;
    private EditText etBuscar;
    private GestorN_Proyect gestorBD;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_productos);

        // incializmaos la bd
        gestorBD = new GestorN_Proyect(this);

        etBuscar = findViewById(R.id.etBuscarProducto);
        recyclerView = findViewById(R.id.rvProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        cargarProductos();

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se usa pero es obligatorio implementarlo
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // se filtran los productos cada vez que cambia el texto
                filtrarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no se usa pero es obligatorio implementarlo
            }
        });
    }

    private void cargarProductos() {
        listaProductos = gestorBD.obtenerTodosProductos();
        adapter = new ProductosAdminAdapter(listaProductos);
        recyclerView.setAdapter(adapter);
    }

    private void filtrarProductos(String texto) {
        List<Producto> listaFiltrada = new ArrayList<>();
        for (Producto producto : listaProductos) {
            // se compara ignorando mays/min
            if (producto.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                    producto.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(producto);
            }
        }
        // actualiza la lista
        adapter.actualizarLista(listaFiltrada);
    }

    class ProductosAdminAdapter extends RecyclerView.Adapter<ProductosAdminAdapter.ProductoViewHolder> {

        private List<Producto> productos;

        public ProductosAdminAdapter(List<Producto> productos) {
            this.productos = productos;
        }

        @Override
        public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_producto, parent, false);
            return new ProductoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductoViewHolder holder, int position) {
            Producto producto = productos.get(position);
            holder.tvNombre.setText(producto.getNombre());
            holder.tvDescripcion.setText(producto.getDescripcion());
            holder.tvPrecio.setText(String.format("€%.2f", producto.getPrecio()));

            // Configuramos los botones editar y eliminar
            holder.btnEditar.setOnClickListener(v -> mostrarDialogoEdicion(producto));
            holder.btnEliminar.setOnClickListener(v -> confirmarEliminacion(producto));
        }

        @Override
        public int getItemCount() {
            return productos.size();
        }

        // actualiza la lista
        public void actualizarLista(List<Producto> nuevaLista) {
            productos = nuevaLista;
            notifyDataSetChanged();
        }

        class ProductoViewHolder extends RecyclerView.ViewHolder {
            TextView tvNombre, tvDescripcion, tvPrecio;
            Button btnEditar, btnEliminar;

            public ProductoViewHolder(View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvNombre);
                tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
                tvPrecio = itemView.findViewById(R.id.tvPrecio);
                btnEditar = itemView.findViewById(R.id.btnEditar);
                btnEliminar = itemView.findViewById(R.id.btnEliminar);
            }
        }

        // muestra el dialogo para editar el producto
        private void mostrarDialogoEdicion(Producto producto) {
            AlertDialog.Builder builder = new AlertDialog.Builder(admin_productos.this);
            View view = LayoutInflater.from(admin_productos.this).inflate(R.layout.admin_dialogo_editar_producto, null);

            EditText etNombre = view.findViewById(R.id.etNombre);
            EditText etDescripcion = view.findViewById(R.id.etDescripcion);
            EditText etPrecio = view.findViewById(R.id.etPrecio);

            etNombre.setText(producto.getNombre());
            etDescripcion.setText(producto.getDescripcion());
            etPrecio.setText(String.valueOf(producto.getPrecio()));

            builder.setView(view)
                    .setTitle("Editar Producto")
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        String nuevoNombre = etNombre.getText().toString();
                        String nuevaDesc = etDescripcion.getText().toString();
                        double nuevoPrecio = Double.parseDouble(etPrecio.getText().toString());

                        // actualiza el los datos de la bd
                        if(gestorBD.actualizarProducto(producto.getId(), nuevoNombre, nuevaDesc, nuevoPrecio)) {
                            producto.setNombre(nuevoNombre);
                            producto.setDescripcion(nuevaDesc);
                            producto.setPrecio(nuevoPrecio);
                            notifyDataSetChanged();
                            Toast.makeText(admin_productos.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }

        // dialogo para borrar
        private void confirmarEliminacion(Producto producto) {
            new AlertDialog.Builder(admin_productos.this)
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Estás seguro de eliminar este producto?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        if(gestorBD.eliminarProducto(producto.getId())) {
                            productos.remove(producto);
                            notifyDataSetChanged();
                            Toast.makeText(admin_productos.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    }
}
