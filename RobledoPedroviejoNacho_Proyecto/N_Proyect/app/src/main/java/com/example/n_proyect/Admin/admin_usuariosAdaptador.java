// adaptador para mostrar y gestionar usuarios en el panel de administrador
package com.example.n_proyect.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n_proyect.BD.GestorN_Proyect;
import com.example.n_proyect.Clases.Usuario;
import com.example.n_proyect.R;

import java.util.List;

public class admin_usuariosAdaptador extends RecyclerView.Adapter<admin_usuariosAdaptador.UsuarioViewHolder> {

    private Context contexto;
    private List<Usuario> listaUsuarios;
    private GestorN_Proyect gestorBD;

    public admin_usuariosAdaptador(Context contexto, List<Usuario> listaUsuarios, GestorN_Proyect gestorBD) {
        this.contexto = contexto;
        this.listaUsuarios = listaUsuarios;
        this.gestorBD = gestorBD;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.admin_item_usuario, parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int posicion) {
        Usuario usuario = listaUsuarios.get(posicion);

        holder.textoCorreo.setText(usuario.getCorreo());
        holder.textoRol.setText("Rol: " + usuario.getRol());

        holder.botonEditar.setOnClickListener(v -> mostrarDialogoEditar(usuario, posicion));
        holder.botonEliminar.setOnClickListener(v -> confirmarEliminacion(usuario, posicion));
    }

    // cant de usuarios
    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textoCorreo, textoRol;
        Button botonEditar, botonEliminar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textoCorreo = itemView.findViewById(R.id.textoCorreo);
            textoRol = itemView.findViewById(R.id.textoRol);
            botonEditar = itemView.findViewById(R.id.botonEditar);
            botonEliminar = itemView.findViewById(R.id.botonEliminar);
        }
    }

    private void mostrarDialogoEditar(Usuario usuario, int posicion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View vistaDialogo = inflater.inflate(R.layout.admin_dialogo_editar_usuario, null);

        TextView textoCorreo = vistaDialogo.findViewById(R.id.textoCorreoUsuario);
        Spinner spinnerRoles = vistaDialogo.findViewById(R.id.spinnerRoles);
        Button botonGuardar = vistaDialogo.findViewById(R.id.botonGuardarCambios);

        textoCorreo.setText(usuario.getCorreo());

        // llenar spinner con los roles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                contexto,
                R.array.roles_usuario,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);

        // seleccionar el rol actual
        int posicionRol = adapter.getPosition(usuario.getRol());
        spinnerRoles.setSelection(posicionRol >= 0 ? posicionRol : 0);

        // mostrar el diálogo
        AlertDialog dialogo = builder.setView(vistaDialogo).create();
        dialogo.show();

        // al guardar, actualizar el rol
        botonGuardar.setOnClickListener(v -> {
            String nuevoRol = spinnerRoles.getSelectedItem().toString();
            boolean actualizado = gestorBD.actualizarRolUsuario(usuario.getId(), nuevoRol);

            if (actualizado) {
                usuario.setRol(nuevoRol);
                notifyItemChanged(posicion);
                Toast.makeText(contexto, "Usuario actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(contexto, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }

            dialogo.dismiss();
        });
    }

    private void confirmarEliminacion(Usuario usuario, int posicion) {
        new AlertDialog.Builder(contexto)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar al usuario " + usuario.getCorreo() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    boolean eliminado = gestorBD.eliminarUsuario(usuario.getId());
                    if (eliminado) {
                        listaUsuarios.remove(posicion);
                        notifyItemRemoved(posicion);
                        Toast.makeText(contexto, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexto, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
