package com.example.n_proyect.Cliente;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.ProductoImagen;
import com.example.n_proyect.Clases.Producto;
import com.example.n_proyect.R;
import com.example.n_proyect.Clases.StockExistencias;
import com.example.n_proyect.databinding.ClienteDetalleProductoBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class cliente_datosProducto extends AppCompatActivity {

    private ClienteDetalleProductoBinding binding;
    private GestorN_Proyect gestorBD;
    private int productoId;
    private String correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ClienteDetalleProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // obtener id producto y correo usuario
        productoId = getIntent().getIntExtra("PRODUCTO_ID", -1);
        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();

        if (correoUsuario == null) {
            finish();
            return;
        }

        gestorBD = new GestorN_Proyect(this);

        // cargar datos producto y tallas
        cargarDetallesProducto();
        cargarTallasDisponibles();
        configurarBotonAnadir();
    }

    private void cargarDetallesProducto() {
        Producto producto = gestorBD.obtenerProductoPorId(productoId);
        if (producto != null) {
            binding.txtNombre.setText(producto.getNombre());
            binding.txtPrecio.setText(String.format("€%.2f", producto.getPrecio()));
            binding.txtDescripcion.setText(producto.getDescripcion());

            List<String> imagenes = new ArrayList<>();
            imagenes.add(producto.getImagenPrincipal());
            imagenes.add(producto.getImagenSecundaria());

            ProductoImagen adapter = new ProductoImagen(imagenes);
            binding.viewPagerImagenes.setAdapter(adapter);
        }
    }

    // mostrar tallas disponibles o agotadas
    private void cargarTallasDisponibles() {
        GridLayout gridTallas = binding.radioGroupTallas;
        TextView txtSinStock = binding.txtSinStock;
        List<StockExistencias> stock = gestorBD.obtenerStockPorProducto(productoId);

        gridTallas.removeAllViews();
        boolean hayStock = false;

        // orden de tallas
        List<String> ordenTallas = Arrays.asList("XS", "S", "M", "L", "XL", "XXL");
        stock.sort(Comparator.comparingInt(item -> ordenTallas.indexOf(item.getTalla())));

        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        for (StockExistencias item : stock) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);

            if (item.isAgotado()) {
                // mostrar talla agotada
                TextView tv = new TextView(this);
                tv.setText(item.getTalla() + "\nAGOTADO");
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tv.setTextColor(ContextCompat.getColor(this, R.color.rojo_error));
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.bg_talla_agotada);
                tv.setPadding(margin, margin, margin, margin);
                gridTallas.addView(tv, params);
            } else {
                // selecionar talla
                RadioButton rb = new RadioButton(this);
                rb.setText(item.getTalla());
                rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                rb.setTextColor(createColorStateList(
                        ContextCompat.getColor(this, R.color.colorPrimary),
                        ContextCompat.getColor(this, R.color.black)
                ));
                rb.setGravity(Gravity.CENTER);
                rb.setButtonDrawable(null);
                rb.setBackgroundResource(R.drawable.bg_talla_selector);
                rb.setPadding(margin, margin, margin, margin);

                // deseleccionar tallas
                rb.setOnClickListener(v -> {
                    for (int i = 0; i < gridTallas.getChildCount(); i++) {
                        View child = gridTallas.getChildAt(i);
                        if (child instanceof RadioButton && child != v) {
                            ((RadioButton) child).setChecked(false);
                        }
                    }
                });

                gridTallas.addView(rb, params);
                hayStock = true;
            }
        }

        // mensaje si hay stock
        txtSinStock.setVisibility(hayStock ? View.GONE : View.VISIBLE);
        binding.btnAnadir.setEnabled(hayStock);
        binding.btnAnadir.setBackgroundTintList(ColorStateList.valueOf(
                hayStock ? ContextCompat.getColor(this, R.color.colorPrimary) :
                        ContextCompat.getColor(this, R.color.gris_deshabilitado)
        ));
    }

    private ColorStateList createColorStateList(int selectedColor, int normalColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };
        int[] colors = new int[]{selectedColor, normalColor};
        return new ColorStateList(states, colors);
    }

    // bton añadir producto
    private void configurarBotonAnadir() {
        binding.btnAnadir.setOnClickListener(v -> {
            String tallaSeleccionada = obtenerTallaSeleccionada();

            if (tallaSeleccionada == null) {
                Toast.makeText(this, "Selecciona una talla", Toast.LENGTH_SHORT).show();
                return;
            }

            int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);
            int resultado = gestorBD.agregarAlCarrito(usuarioId, productoId, tallaSeleccionada, 1);

            // mostrar mensajes
            switch (resultado) {
                case 1:
                    Toast.makeText(this, "Añadido al carrito", Toast.LENGTH_SHORT).show();
                    deseleccionarTodasLasTallas();
                    cargarTallasDisponibles();
                    break;
                case -1:
                    Toast.makeText(this, "No hay suficiente stock", Toast.LENGTH_SHORT).show();
                    cargarTallasDisponibles();
                    break;
                default:
                    Toast.makeText(this, "Error al añadir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // obtener talla seleccionada
    private String obtenerTallaSeleccionada() {
        GridLayout grid = binding.radioGroupTallas;
        for (int i = 0; i < grid.getChildCount(); i++) {
            View child = grid.getChildAt(i);
            if (child instanceof RadioButton && ((RadioButton) child).isChecked()) {
                return ((RadioButton) child).getText().toString();
            }
        }
        return null;
    }

    // deseleccionar todas las tallas
    private void deseleccionarTodasLasTallas() {
        GridLayout grid = binding.radioGroupTallas;
        for (int i = 0; i < grid.getChildCount(); i++) {
            View child = grid.getChildAt(i);
            if (child instanceof RadioButton) {
                ((RadioButton) child).setChecked(false);
            }
        }
    }
}