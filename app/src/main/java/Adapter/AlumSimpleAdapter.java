package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Entidades.Alumno;
import com.aplicacion.gestion_escolar.R;
public class AlumSimpleAdapter extends RecyclerView.Adapter<AlumSimpleAdapter.AlumnoViewHolder> {
    private final List<Alumno> listaAlumnos;

    public AlumSimpleAdapter(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno_simple, parent, false);
        return new AlumnoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        Alumno alumno = listaAlumnos.get(position);
        holder.tvNombreAlumno.setText(alumno.getApellido() + ", " + alumno.getNombre());
        holder.tvDniAlumno.setText("DNI: " + alumno.getDni());
    }

    @Override
    public int getItemCount() {
        return listaAlumnos == null ? 0 : listaAlumnos.size();
    }
    static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreAlumno, tvDniAlumno;
        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAlumno = itemView.findViewById(R.id.tvNombreAlumnoItem);
            tvDniAlumno = itemView.findViewById(R.id.tvDniAlumnoItem);
        }
    }
}
