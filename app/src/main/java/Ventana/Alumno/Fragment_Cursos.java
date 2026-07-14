package Ventana.Alumno;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import Adapter.MateriaAdapter;
import Datos.AdminSQLiteOpenHelper;
import Datos.UsuarioDao;
import Entidades.Materia;

public class Fragment_Cursos extends Fragment {

    private static final String NOMBRE_BD = "BD_Sygemy";
    private static final int VERSION_BD = 1;

    private TextView tvStudentName, tvGradeName,tvSubjectCount, tvEmptySubjects;
    private ChipGroup chipGroupFilter;
    private Chip chipAll, chipInProgress, chipApproved;
    private RecyclerView rvSubjects;
    private MateriaAdapter adapter;
    private final List<Materia> todasLasMaterias = new ArrayList<>();

    private AdminSQLiteOpenHelper helper;
    private SQLiteDatabase baseDeDatos;

    public Fragment_Cursos() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__cursos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enlazarVistas(view);
        configurarRecyclerView();
        configurarFiltros();
        cargarMateriasDelAlumno();
    }
    private void enlazarVistas(View view) {
        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvGradeName = view.findViewById(R.id.tvGradeName);
        tvSubjectCount = view.findViewById(R.id.tvSubjectCount);
        chipGroupFilter = view.findViewById(R.id.chipGroupFilter);
        chipAll = view.findViewById(R.id.chipAll);
        chipInProgress = view.findViewById(R.id.chipInProgress);
        chipApproved = view.findViewById(R.id.chipApproved);
        rvSubjects = view.findViewById(R.id.rvSubjects);
        tvEmptySubjects = view.findViewById(R.id.tvEmptySubjects);
    }
    private void configurarRecyclerView() {
        adapter = new MateriaAdapter();
        rvSubjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSubjects.setAdapter(adapter);
    }
    private void configurarFiltros() {
        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                aplicarFiltro(chipAll.getId());
                return;
            }
            aplicarFiltro(checkedIds.get(0));
        });
    }
    private void aplicarFiltro(int chipId) {
        List<Materia> filtradas;
        if (chipId == chipInProgress.getId()) {
            filtradas = filtrarPorEstado(Materia.ESTADO_EN_CURSO);
        } else if (chipId == chipApproved.getId()) {
            filtradas = filtrarPorEstado(Materia.ESTADO_APROBADA);
        } else {
            filtradas = todasLasMaterias;
        }
        mostrarMaterias(filtradas);
    }
    private List<Materia> filtrarPorEstado(String estado) {
        List<Materia> resultado = new ArrayList<>();
        for (Materia materia : todasLasMaterias) {
            if (estado.equals(materia.getEstado())) {
                resultado.add(materia);
            }
        }
        return resultado;
    }
    private void mostrarMaterias(List<Materia> materias) {
        adapter.setMaterias(materias);
        boolean vacio = materias.isEmpty();
        tvEmptySubjects.setVisibility(vacio ? View.VISIBLE : View.GONE);
        rvSubjects.setVisibility(vacio ? View.GONE : View.VISIBLE);
    }
    private void cargarMateriasDelAlumno() {
        int alumnoId = UsuarioDao.obtenerAlumnoId(requireContext());
        if (alumnoId == -1) {
            Toast.makeText(requireContext(),
                    "No se encontro la sesion del alumno. Volve a iniciar sesion.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        helper = new AdminSQLiteOpenHelper(requireContext(), NOMBRE_BD, null, VERSION_BD);
        baseDeDatos = helper.getReadableDatabase();

        DatosAlumno datosAlumno = obtenerDatosAlumno(alumnoId);
        if (datosAlumno == null) {
            Toast.makeText(requireContext(), "No se encontro el alumno.", Toast.LENGTH_LONG).show();
            return;
        }

        tvStudentName.setText(datosAlumno.nombreCompleto);
        tvGradeName.setText(datosAlumno.curso);

        List<Materia> materias = obtenerMateriasDelCurso(datosAlumno.curso, alumnoId);
        todasLasMaterias.clear();
        todasLasMaterias.addAll(materias);

        tvSubjectCount.setText(materias.size() + " materia" + (materias.size() == 1 ? "" : "s") + " este ciclo lectivo");

        mostrarMaterias(todasLasMaterias);
    }
    private DatosAlumno obtenerDatosAlumno(int alumnoId) {
        DatosAlumno datos = null;
        Cursor cursor = baseDeDatos.rawQuery(
                "SELECT nombre_alum, apellido_alum, curso_alum FROM alumnos WHERE id = ?",
                new String[]{String.valueOf(alumnoId)});
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String apellido = cursor.getString(1);
            String curso = cursor.getString(2);
            datos = new DatosAlumno(nombre + " " + apellido, curso);
        }
        cursor.close();
        return datos;
    }
    private List<Materia> obtenerMateriasDelCurso(String curso, int alumnoId) {
        List<Materia> materias = new ArrayList<>();

        Cursor cursor = baseDeDatos.rawQuery(
                "SELECT DISTINCT h.materia_hor, p.nombre_prof, p.apellido_prof " +
                        "FROM horarios h " +
                        "LEFT JOIN profesores p ON h.profesor_id = p.id " +
                        "WHERE h.curso_hor = ? " +
                        "ORDER BY h.materia_hor",
                new String[]{curso});

        while (cursor.moveToNext()) {
            String nombreMateria = cursor.getString(0);
            String nombreProf = cursor.getString(1);
            String apellidoProf = cursor.getString(2);

            String profesorCompleto = null;
            if (nombreProf != null && apellidoProf != null) {
                profesorCompleto = nombreProf + " " + apellidoProf;
            }

            Double promedio = obtenerPromedioMateria(alumnoId, nombreMateria);
            String estado = (promedio != null && promedio >= 6.0) ? Materia.ESTADO_APROBADA : Materia.ESTADO_EN_CURSO;

            materias.add(new Materia(nombreMateria, profesorCompleto, estado, promedio));
        }
        cursor.close();
        return materias;
    }

    @Nullable
    private Double obtenerPromedioMateria(int alumnoId, String materia) {
        Double promedio = null;
        Cursor cursor = baseDeDatos.rawQuery(
                "SELECT AVG(nota) FROM calificaciones WHERE alumno_id = ? AND materia_cal = ?",
                new String[]{String.valueOf(alumnoId), materia});
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            promedio = cursor.getDouble(0);
        }
        cursor.close();
        return promedio;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (baseDeDatos != null && baseDeDatos.isOpen()) {
            baseDeDatos.close();
        }
    }
    private static class DatosAlumno {
        final String nombreCompleto;
        final String curso;

        DatosAlumno(String nombreCompleto, String curso) {
            this.nombreCompleto = nombreCompleto;
            this.curso = curso;
        }
    }
}