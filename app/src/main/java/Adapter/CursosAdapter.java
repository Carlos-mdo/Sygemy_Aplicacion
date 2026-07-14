package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Entidades.Cursos;
import com.aplicacion.gestion_escolar.R;

public class CursosAdapter extends RecyclerView.Adapter<CursosAdapter.CursoViewHolder> {

    private List<Cursos> listaCursos;

    public CursosAdapter(List<Cursos> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public void actualizarLista(List<Cursos> nuevaLista) {
        this.listaCursos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso_profe, parent, false);
        return new CursoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        Cursos curso = listaCursos.get(position);
        holder.tvNombreCurso.setText(curso.getNombreCurso());
        holder.tvMateriaCurso.setText(curso.getMateria());
        holder.tvHorarioCurso.setText(curso.getDias() + " - " + curso.getHoraInicio() + " a " + curso.getHoraFin());
        holder.tvCantidadAlumnosCurso.setText(curso.getCantidadAlumnos() + " alumnos");
    }

    @Override
    public int getItemCount() {
        return listaCursos == null ? 0 : listaCursos.size();
    }

    static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso, tvMateriaCurso, tvHorarioCurso, tvCantidadAlumnosCurso;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvMateriaCurso = itemView.findViewById(R.id.tvMateriaCurso);
            tvHorarioCurso = itemView.findViewById(R.id.tvHorarioCurso);
            tvCantidadAlumnosCurso = itemView.findViewById(R.id.tvCantidadAlumnosCurso);
        }
    }
}
