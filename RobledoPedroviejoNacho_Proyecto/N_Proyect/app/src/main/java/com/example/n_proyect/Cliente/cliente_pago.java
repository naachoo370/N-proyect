package com.example.n_proyect.Cliente;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.ControlSesion;
import com.example.n_proyect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;

public class cliente_pago extends AppCompatActivity {

    private TextInputEditText etDireccion, etNumeroTarjeta, etCaducidad, etCVV, etTelefonoBizum;
    private LinearLayout layoutTarjetaContainer;
    private TextInputLayout layoutBizum;
    private Spinner spinnerMetodoPago;
    private GestorN_Proyect gestorBD;
    private String correoUsuario;
    private double totalPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_pago);

        totalPedido = getIntent().getDoubleExtra("TOTAL_PEDIDO", 0.0);
        ControlSesion sesionManager = new ControlSesion(this);
        correoUsuario = sesionManager.obtenerCorreo();
        gestorBD = new GestorN_Proyect(this);

        inicializarVistas();
        configurarAutoformatoFecha();
        cargarDatosUsuario();
        configurarSpinner();
        configurarBotonConfirmar();
    }

    // vincular vistas del layout
    private void inicializarVistas() {
        etDireccion = findViewById(R.id.etDireccion);
        etNumeroTarjeta = findViewById(R.id.etNumeroTarjeta);
        etCaducidad = findViewById(R.id.etCaducidad);
        etCVV = findViewById(R.id.etCVV);
        etTelefonoBizum = findViewById(R.id.etTelefonoBizum);
        layoutTarjetaContainer = findViewById(R.id.layoutTarjetaContainer);
        layoutBizum = findViewById(R.id.layoutBizum);
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago);
    }

    // añidr la barra de fecha sola
    private void configurarAutoformatoFecha() {
        etCaducidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String current = s.toString();
                if (current.length() == 2 && !current.contains("/")) {
                    etCaducidad.setText(current + "/");
                    etCaducidad.setSelection(3);
                } else if (current.length() > 5) {
                    etCaducidad.setText(current.substring(0, 5));
                    etCaducidad.setSelection(5);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // cargar dirección y teléfono
    private void cargarDatosUsuario() {
        int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);
        Cursor cursor = gestorBD.obtenerUsuarioPorId(usuarioId);

        if(cursor != null && cursor.moveToFirst()) {
            int colDireccion = cursor.getColumnIndex("direccion");
            int colTelefono = cursor.getColumnIndex("telefono");

            if(colDireccion != -1 && !cursor.isNull(colDireccion)) {
                etDireccion.setText(cursor.getString(colDireccion));
            }

            if(colTelefono != -1 && !cursor.isNull(colTelefono)) {
                etTelefonoBizum.setText(cursor.getString(colTelefono));
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_SHORT).show();
        }
    }

    // spinner metodo pago
    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.metodos_pago,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetodoPago.setAdapter(adapter);

        spinnerMetodoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarVistasMetodoPago(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // mostrar campos segun el metodo
    private void actualizarVistasMetodoPago(int position) {
        layoutTarjetaContainer.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        layoutBizum.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
    }

    // btn pago
    private void configurarBotonConfirmar() {
        Button btnConfirmar = findViewById(R.id.btnConfirmarPago);
        btnConfirmar.setOnClickListener(v -> validarYProcesarPago());
    }

    // todos los campos bien
    private void validarYProcesarPago() {
        if(validarCamposComunes() && validarMetodoPagoEspecifico()) {
            guardarDatosUsuario();
            procesarPedido();
        }
    }

    // validar dirección
    private boolean validarCamposComunes() {
        String direccion = etDireccion.getText().toString().trim();

        if(direccion.isEmpty()) {
            etDireccion.setError("Dirección requerida");
            return false;
        }
        return true;
    }

    // validar según el metodo de pago elegido
    private boolean validarMetodoPagoEspecifico() {
        return spinnerMetodoPago.getSelectedItemPosition() == 0 ?
                validarTarjeta() : validarBizum();
    }

    // validar datos de tarjeta
    private boolean validarTarjeta() {
        String numero = etNumeroTarjeta.getText().toString().trim();
        String caducidad = etCaducidad.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();

        if(numero.length() != 16) {
            etNumeroTarjeta.setError("Número inválido");
            return false;
        }

        if(!validarFormatoCaducidad(caducidad)) {
            etCaducidad.setError("Formato MM/YY");
            return false;
        }

        if(cvv.length() != 3) {
            etCVV.setError("CVV inválido");
            return false;
        }

        return validarFechaFutura(caducidad);
    }

    // validar formato MM/YY
    private boolean validarFormatoCaducidad(String caducidad) {
        return caducidad.matches("^(0[1-9]|1[0-2])/\\d{2}$");
    }

    // comprobar la fecha de caducidad
    private boolean validarFechaFutura(String caducidad) {
        try {
            String[] partes = caducidad.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            Calendar ahora = Calendar.getInstance();
            int anoActual = ahora.get(Calendar.YEAR) % 100;
            int mesActual = ahora.get(Calendar.MONTH) + 1;

            if(ano < anoActual || (ano == anoActual && mes < mesActual)) {
                etCaducidad.setError("Fecha caducada");
                return false;
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // validar número de teléfono para bizum
    private boolean validarBizum() {
        String telefono = etTelefonoBizum.getText().toString().trim();

        if(telefono.length() != 9) {
            etTelefonoBizum.setError("Teléfono inválido");
            return false;
        }
        return true;
    }

    // guardar dirección y teléfono si es necesario
    private void guardarDatosUsuario() {
        int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);
        String direccion = etDireccion.getText().toString();
        gestorBD.actualizarDireccionUsuario(usuarioId, direccion);

        if(spinnerMetodoPago.getSelectedItemPosition() == 1) {
            String telefono = etTelefonoBizum.getText().toString();
            gestorBD.actualizarTelefonoUsuario(usuarioId, telefono);
        }
    }

    // procesar el pedido y vaciar carrito
    private void procesarPedido() {
        int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);

        boolean exito = gestorBD.crearPedido(
                usuarioId,
                totalPedido,
                etDireccion.getText().toString()
        );

        if (exito) {
            guardarMetodoPago();
            gestorBD.vaciarCarritoPorUsuario(correoUsuario);

            Toast.makeText(this, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error al procesar el pedido", Toast.LENGTH_SHORT).show();
        }
    }

    // guardar los datos del metodo de pago en la base de datos
    private void guardarMetodoPago() {
        int usuarioId = gestorBD.obtenerIdUsuarioPorCorreo(correoUsuario);

        if(spinnerMetodoPago.getSelectedItemPosition() == 0) {
            String numero = etNumeroTarjeta.getText().toString();
            gestorBD.insertarMetodoPago(
                    usuarioId,
                    "tarjeta",
                    numero.substring(numero.length() - 4),
                    etCaducidad.getText().toString(),
                    etCVV.getText().toString(),
                    null
            );
        } else {
            gestorBD.insertarMetodoPago(
                    usuarioId,
                    "bizum",
                    null,
                    null,
                    null,
                    etTelefonoBizum.getText().toString()
            );
        }
    }

    // cerrar conexión
    protected void onDestroy() {
        gestorBD.close();
        super.onDestroy();
    }
}
