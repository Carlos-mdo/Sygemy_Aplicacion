package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Profesor;

public class ProfesorAdapter extends RecyclerView.Adapter<ProfesorAdapter.ViewHolder> {

    private List<Profesor> listaProfesores;

    public ProfesorAdapter(List<Profesor> listaProfesores) {
        this.listaProfesores = listaProfesores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profesor, parent, false);

        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Profesor profesor = listaProfesores.get(position);

        holder.txtNombre.setText(
                profesor.getNombre() + " " + profesor.getApellido()
        );

        holder.txtMateria.setText(
                profesor.getMateria()
        );
    }

    @Override
    public int getItemCount() {
        return listaProfesores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtMateria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtMateria = itemView.findViewById(R.id.txtMateria);
        }
    }
}