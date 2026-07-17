package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.MateriaSeccion;

public class MateriaSeccionAdapter extends RecyclerView.Adapter<MateriaSeccionAdapter.SeccionViewHolder> {

    private final List<MateriaSeccion> secciones;

    public MateriaSeccionAdapter(List<MateriaSeccion> secciones) {
        this.secciones = secciones;
    }

    @NonNull
    @Override
    public SeccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materia_seccion, parent, false);
        return new SeccionViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull SeccionViewHolder holder, int position) {
        MateriaSeccion seccion = secciones.get(position);

        String titulo = seccion.getMateria() + " (" + seccion.getAlumnos().size() + " alumnos)";
        if (seccion.isEsMateriaDelProfesor()) {
            titulo += " · Tu materia";
        }
        holder.tvMateriaHeader.setText(titulo);

        if (seccion.getAlumnos().isEmpty()) {
            holder.tvSinAlumnosSeccion.setVisibility(View.VISIBLE);
            holder.rvAlumnosSeccion.setVisibility(View.GONE);
        } else {
            holder.tvSinAlumnosSeccion.setVisibility(View.GONE);
            holder.rvAlumnosSeccion.setVisibility(View.VISIBLE);
            holder.rvAlumnosSeccion.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.rvAlumnosSeccion.setNestedScrollingEnabled(false);
            holder.rvAlumnosSeccion.setAdapter(new AlumSimpleAdapter(seccion.getAlumnos()));
        }
    }

    @Override
    public int getItemCount() {
        return secciones == null ? 0 : secciones.size();
    }

    static class SeccionViewHolder extends RecyclerView.ViewHolder {
        TextView tvMateriaHeader, tvSinAlumnosSeccion;
        RecyclerView rvAlumnosSeccion;

        SeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMateriaHeader = itemView.findViewById(R.id.tvMateriaHeader);
            tvSinAlumnosSeccion = itemView.findViewById(R.id.tvSinAlumnosSeccion);
            rvAlumnosSeccion = itemView.findViewById(R.id.rvAlumnosSeccion);
        }
    }
}
