package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
        ImageButton btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtRol = itemView.findViewById(R.id.txtRol);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
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


            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(20, 20, 20, 20);

            EditText inputNombre = new EditText(context);
            inputNombre.setHint("Usuario");
            inputNombre.setText(usuario.getUsuario());

            EditText inputPass = new EditText(context);
            inputPass.setHint("Contraseña");
            inputPass.setText(usuario.getContrasenia());

            EditText inputRol = new EditText(context);
            inputRol.setHint("Rol");
            inputRol.setText(usuario.getRol());

            layout.addView(inputNombre);
            layout.addView(inputPass);
            layout.addView(inputRol);

            new AlertDialog.Builder(context)
                    .setTitle("Editar usuario")
                    .setView(layout)

                    .setPositiveButton("Guardar", (dialog, which) -> {

                        String nuevoNombre = inputNombre.getText().toString();
                        String nuevaPass = inputPass.getText().toString();
                        String nuevoRol = inputRol.getText().toString();

                        if (nuevoNombre.isEmpty() || nuevaPass.isEmpty() || nuevoRol.isEmpty()) {
                            Toast.makeText(context, "Completar todos los campos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        usuario.setUsuario(nuevoNombre);
                        usuario.setContrasenia(nuevaPass);
                        usuario.setRol(nuevoRol);

                        servicio.actualizarUsuario(usuario);

                        notifyItemChanged(holder.getAdapterPosition());

                        Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                    })

                    .setNegativeButton("Cancelar", null)
                    .show();
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
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}