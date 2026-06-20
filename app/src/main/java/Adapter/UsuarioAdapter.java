package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Usuarios;
import Servicios.ServicioAdmin;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    List<Usuarios> lista;
    ServicioAdmin servicio;

    public UsuarioAdapter(List<Usuarios> lista, ServicioAdmin servicio) {
        this.lista = lista;
        this.servicio = servicio;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtRol;
        ImageButton btnEditar, btnEliminar, btnInformacion;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtRol = itemView.findViewById(R.id.txtRol);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnInformacion = itemView.findViewById(R.id.btnInformacion);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Usuarios usuario = lista.get(position);

        holder.txtNombre.setText(usuario.getUsuario());
        holder.txtRol.setText(usuario.getRol());


        holder.btnEditar.setOnClickListener(v -> {

            Context context = v.getContext();

            mostrarDialogoEditar(context, usuario, holder);

        });

        holder.btnEliminar.setOnClickListener(v -> {

            Context context = v.getContext();

            new AlertDialog.Builder(context)
                    .setTitle("Eliminar")
                    .setMessage("¿Seguro que querés eliminar este usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        int pos = holder.getAdapterPosition();

                        servicio.eliminarUsuario(usuario.getId());

                        lista.remove(pos);
                        notifyItemRemoved(pos);

                        Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.btnInformacion.setOnClickListener(v -> {

            Context context = v.getContext();

            String info =
                    "ID: " + usuario.getId() + "\n\n" +
                            "Usuario: " + usuario.getUsuario() + "\n\n" +
                            "Contraseña: " + usuario.getContrasenia() + "\n\n" +
                            "Rol: " + usuario.getRol();

            new AlertDialog.Builder(context)
                    .setTitle("Información del usuario")
                    .setMessage(info)

                    .setPositiveButton("Editar", (dialog, which) -> {

                        mostrarDialogoEditar(context, usuario, holder);

                    })

                    .setNegativeButton("Cerrar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
    private void mostrarDialogoEditar(Context context, Usuarios usuario, ViewHolder holder) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        EditText inputNombre = new EditText(context);
        inputNombre.setHint("Usuario");
        inputNombre.setText(usuario.getUsuario());

        EditText inputPass = new EditText(context);
        inputPass.setHint("Contraseña");
        inputPass.setText(usuario.getContrasenia());

        Spinner spinnerRol = new Spinner(context);

        String roles[] = {"admin", "profesor", "alumno"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                roles
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRol.setAdapter(adapter);

        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equalsIgnoreCase(usuario.getRol())) {
                spinnerRol.setSelection(i);
                break;
            }
        }

        layout.addView(inputNombre);
        layout.addView(inputPass);
        layout.addView(spinnerRol);

        new AlertDialog.Builder(context)
                .setTitle("Editar usuario")
                .setView(layout)

                .setPositiveButton("Guardar", (dialog, which) -> {

                    String nuevoNombre = inputNombre.getText().toString();
                    String nuevaPass = inputPass.getText().toString();
                    String nuevoRol = spinnerRol.getSelectedItem().toString();

                    if (nuevoNombre.isEmpty() || nuevaPass.isEmpty()) {

                        Toast.makeText(context,
                                "Completar todos los campos",
                                Toast.LENGTH_SHORT).show();

                        return;
                    }

                    usuario.setUsuario(nuevoNombre);
                    usuario.setContrasenia(nuevaPass);
                    usuario.setRol(nuevoRol);

                    servicio.actualizarUsuario(usuario);

                    notifyItemChanged(holder.getAdapterPosition());

                    Toast.makeText(context,
                            "Usuario actualizado",
                            Toast.LENGTH_SHORT).show();
                })

                .setNegativeButton("Cancelar", null)
                .show();
    }
}