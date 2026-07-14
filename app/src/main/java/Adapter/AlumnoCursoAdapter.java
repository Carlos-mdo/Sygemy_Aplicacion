package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Alumno;

public class AlumnoCursoAdapter extends RecyclerView.Adapter<AlumnoCursoAdapter.AlumnoViewHolder> {

    private List<Alumno> listaAlumnos;

    public AlumnoCursoAdapter(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    public void actualizarLista(List<Alumno> nuevaLista) {
        this.listaAlumnos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alumno_curso, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        Alumno alumno = listaAlumnos.get(position);
        holder.tvNombreAlumno.setText(alumno.getNombre() + " " + alumno.getApellido());
        holder.tvDniAlumno.setText("DNI: " + alumno.getDni());
        holder.tvGeneroAlumno.setText(alumno.getGenero() != null ? alumno.getGenero() : "");
    }

    @Override
    public int getItemCount() {
        return listaAlumnos == null ? 0 : listaAlumnos.size();
    }

    static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreAlumno, tvDniAlumno, tvGeneroAlumno;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAlumno = itemView.findViewById(R.id.tvNombreAlumno);
            tvDniAlumno = itemView.findViewById(R.id.tvDniAlumno);
            tvGeneroAlumno = itemView.findViewById(R.id.tvGeneroAlumno);
        }
    }
}
