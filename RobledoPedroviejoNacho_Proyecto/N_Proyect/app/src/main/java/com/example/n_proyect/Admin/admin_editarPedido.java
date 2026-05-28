package com.example.n_proyect.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.Pedido;
import com.example.n_proyect.R;

public class admin_editarPedido extends DialogFragment {

    private Pedido pedido;
    private EditText etDireccion;
    private Spinner spinnerEstado;


    public static admin_editarPedido newInstance(Pedido pedido) {
        admin_editarPedido fragment = new admin_editarPedido();
        Bundle args = new Bundle();
        args.putSerializable("pedido", pedido);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.admin_dialogo_editar_pedido, null);

        // obtener pedido del bundle
        if (getArguments() != null) {
            pedido = (Pedido) getArguments().getSerializable("pedido");
        }


        etDireccion = view.findViewById(R.id.etDireccion);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnEliminar = view.findViewById(R.id.btnEliminar);

        // cargar estados del pedido en el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.estados_pedido, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // mostrar datos
        if (pedido != null) {
            etDireccion.setText(pedido.getDireccionEnvio());
            spinnerEstado.setSelection(adapter.getPosition(pedido.getEstado()));
        }

        // guardar cambios
        btnGuardar.setOnClickListener(v -> guardarCambios());
        // eliminar pedido
        btnEliminar.setOnClickListener(v -> eliminarPedido());

        builder.setView(view).setTitle("Editar Pedido #" + pedido.getId());
        return builder.create();
    }


    private void guardarCambios() {
        String nuevaDireccion = etDireccion.getText().toString();
        String nuevoEstado = spinnerEstado.getSelectedItem().toString();

        pedido.setDireccionEnvio(nuevaDireccion);
        pedido.setEstado(nuevoEstado);

        GestorN_Proyect gestorBD = new GestorN_Proyect(getContext());
        if (gestorBD.actualizarPedido(pedido)) {
            ((admin_pedidos) getActivity()).actualizarPedido(pedido);
            dismiss(); // cerrar dialogo
        }
    }


    private void eliminarPedido() {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Pedido")
                .setMessage("¿Estás seguro de eliminar este pedido?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    GestorN_Proyect gestorBD = new GestorN_Proyect(getContext());
                    if (gestorBD.eliminarPedido(pedido.getId())) {
                        ((admin_pedidos) getActivity()).eliminarPedidoDeLista(pedido.getId());
                        dismiss();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
