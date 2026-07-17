package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Datos.AlumnosDao;
import Entidades.Alumno;
import Entidades.CursoConMaterias;
import Entidades.MateriaSeccion;
import com.aplicacion.gestion_escolar.R;

public class CursosAdapter extends RecyclerView.Adapter<CursosAdapter.CursoViewHolder> {

    private List<CursoConMaterias> listaCursos;
    private final AlumnosDao alumnosDao;
    private final Set<Integer> posicionesExpandidas = new HashSet<>();
    private final Map<String, List<Alumno>> cacheAlumnosPorCurso = new HashMap<>();

    public CursosAdapter(List<CursoConMaterias> listaCursos, AlumnosDao alumnosDao) {
        this.listaCursos = listaCursos;
        this.alumnosDao = alumnosDao;
    }

    public void actualizarLista(List<CursoConMaterias> nuevaLista) {
        this.listaCursos = nuevaLista;
        posicionesExpandidas.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso_profe, parent, false);
        return new CursoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        CursoConMaterias curso = listaCursos.get(position);
        holder.tvNombreCurso.setText(curso.getNombreCurso());
        holder.tvMateriaCurso.setText("Dictás: " + String.join(", ", curso.getMateriasDelProfesor()));
        holder.tvHorarioCurso.setText(curso.getHorarioResumen());
        holder.tvCantidadAlumnosCurso.setText(curso.getCantidadAlumnos() + " alumnos");

        boolean expandido = posicionesExpandidas.contains(position);
        holder.ivFlecha.setRotation(expandido ? 180f : 0f);

        if (expandido) {
            mostrarMaterias(curso, holder);
        } else {
            holder.rvAlumnos.setVisibility(View.GONE);
            holder.tvSinAlumnos.setVisibility(View.GONE);
        }

        holder.headerContainer.setOnClickListener(v -> {
            if (posicionesExpandidas.contains(position)) {
                posicionesExpandidas.remove(position);
            } else {
                posicionesExpandidas.add(position);
            }
            notifyItemChanged(position);
        });
    }

    private void mostrarMaterias(CursoConMaterias curso, CursoViewHolder holder) {
        String clave = curso.getNombreCurso();
        List<Alumno> alumnos = cacheAlumnosPorCurso.get(clave);
        if (alumnos == null) {
            alumnos = alumnosDao.obtenerAlumnosPorCurso(clave);
            cacheAlumnosPorCurso.put(clave, alumnos);
        }

        if (alumnos.isEmpty()) {
            holder.rvAlumnos.setVisibility(View.GONE);
            holder.tvSinAlumnos.setVisibility(View.VISIBLE);
            return;
        }

        holder.tvSinAlumnos.setVisibility(View.GONE);
        holder.rvAlumnos.setVisibility(View.VISIBLE);

        List<MateriaSeccion> secciones = new ArrayList<>();
        for (String materia : curso.getMateriasDelGrado()) {
            boolean esDelProfesor = curso.getMateriasDelProfesor().stream()
                    .anyMatch(m -> m.equalsIgnoreCase(materia));
            secciones.add(new MateriaSeccion(materia, alumnos, esDelProfesor));
        }

        holder.rvAlumnos.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvAlumnos.setNestedScrollingEnabled(false);
        holder.rvAlumnos.setAdapter(new MateriaSeccionAdapter(secciones));
    }

    @Override
    public int getItemCount() {
        return listaCursos == null ? 0 : listaCursos.size();
    }

    static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso, tvMateriaCurso, tvHorarioCurso, tvCantidadAlumnosCurso, tvSinAlumnos;
        View headerContainer;
        ImageView ivFlecha;
        RecyclerView rvAlumnos;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvMateriaCurso = itemView.findViewById(R.id.tvMateriaCurso);
            tvHorarioCurso = itemView.findViewById(R.id.tvHorarioCurso);
            tvCantidadAlumnosCurso = itemView.findViewById(R.id.tvCantidadAlumnosCurso);
            tvSinAlumnos = itemView.findViewById(R.id.tvSinAlumnos);
            ivFlecha = itemView.findViewById(R.id.ivFlecha);
            rvAlumnos = itemView.findViewById(R.id.rvAlumnos);
        }
    }
}
