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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.R;
import com.example.n_proyect.Clases.StockExistencias;
import com.example.n_proyect.Clases.StockProducto;

import java.util.List;
import java.util.stream.Collectors;

public class admin_stock extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StockAdapter adapter;
    private EditText etBuscar;
    private GestorN_Proyect gestorBD;
    private List<StockProducto> listaStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_stock);

        gestorBD = new GestorN_Proyect(this);
        etBuscar = findViewById(R.id.etBuscarStock);
        recyclerView = findViewById(R.id.rvStock);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarStock();

        // filtrar la lista
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarStock(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // carga el stock y asigna el adaptador
    private void cargarStock() {
        listaStock = gestorBD.obtenerStockProductos();
        adapter = new StockAdapter(listaStock, this::recargarStock);
        recyclerView.setAdapter(adapter);
    }

    private void filtrarStock(String texto) {
        List<StockProducto> listaFiltrada = listaStock.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        p.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
        adapter.actualizarLista(listaFiltrada);  // actualiza
    }

    private void recargarStock() {
        cargarStock();
    }

    // adaptador para mostrae porductos por su dispo
    class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
        private List<StockProducto> stockProductos;
        private final Runnable onStockUpdated;

        public StockAdapter(List<StockProducto> stockProductos, Runnable onStockUpdated) {
            this.stockProductos = stockProductos;
            this.onStockUpdated = onStockUpdated;
        }

        @NonNull
        @Override
        public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_stock, parent, false);
            return new StockViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
            StockProducto producto = stockProductos.get(position);

            holder.tvNombre.setText(producto.getNombre());
            holder.tvDescripcion.setText(producto.getDescripcion());

            holder.etXS.setText("");
            holder.etS.setText("");
            holder.etM.setText("");
            holder.etL.setText("");
            holder.etXL.setText("");
            holder.etXXL.setText("");

            // rellenar cantidades segun talla
            for (StockExistencias item : producto.getStockItems()) {
                switch (item.getTalla()) {
                    case "XS": holder.etXS.setText(String.valueOf(item.getCantidad())); break;
                    case "S": holder.etS.setText(String.valueOf(item.getCantidad())); break;
                    case "M": holder.etM.setText(String.valueOf(item.getCantidad())); break;
                    case "L": holder.etL.setText(String.valueOf(item.getCantidad())); break;
                    case "XL": holder.etXL.setText(String.valueOf(item.getCantidad())); break;
                    case "XXL": holder.etXXL.setText(String.valueOf(item.getCantidad())); break;
                }
            }

            // actualiza bd
            holder.btnGuardar.setOnClickListener(v -> {
                try {
                    boolean correcto = true;
                    correcto &= actualizarCantidad(producto.getId(), "XS", holder.etXS.getText().toString());
                    correcto &= actualizarCantidad(producto.getId(), "S", holder.etS.getText().toString());
                    correcto &= actualizarCantidad(producto.getId(), "M", holder.etM.getText().toString());
                    correcto &= actualizarCantidad(producto.getId(), "L", holder.etL.getText().toString());
                    correcto &= actualizarCantidad(producto.getId(), "XL", holder.etXL.getText().toString());
                    correcto &= actualizarCantidad(producto.getId(), "XXL", holder.etXXL.getText().toString());

                    if (correcto) {
                        Toast.makeText(admin_stock.this, "stock actualizado!", Toast.LENGTH_SHORT).show();
                        onStockUpdated.run();
                    } else {
                        Toast.makeText(admin_stock.this, "error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(admin_stock.this, "error: cantidad inválida", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private boolean actualizarCantidad(int productoId, String talla, String cantidadStr) {
            try {
                int cantidad = Integer.parseInt(cantidadStr.isEmpty() ? "0" : cantidadStr);
                if (cantidad < 0) throw new NumberFormatException();
                return gestorBD.actualizarInventario(productoId, talla, cantidad);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return stockProductos.size();
        }

        public void actualizarLista(List<StockProducto> nuevaLista) {
            stockProductos = nuevaLista;
            notifyDataSetChanged();
        }

        class StockViewHolder extends RecyclerView.ViewHolder {
            TextView tvNombre, tvDescripcion;
            EditText etXS, etS, etM, etL, etXL, etXXL;
            Button btnGuardar;

            public StockViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvNombreProducto);
                tvDescripcion = itemView.findViewById(R.id.tvDescripcionProducto);
                etXS = itemView.findViewById(R.id.etCantidadXS);
                etS = itemView.findViewById(R.id.etCantidadS);
                etM = itemView.findViewById(R.id.etCantidadM);
                etL = itemView.findViewById(R.id.etCantidadL);
                etXL = itemView.findViewById(R.id.etCantidadXL);
                etXXL = itemView.findViewById(R.id.etCantidadXXL);
                btnGuardar = itemView.findViewById(R.id.btnGuardarStock);
            }
        }
    }
}
