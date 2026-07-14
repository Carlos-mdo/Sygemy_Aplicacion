package Ventana.Admin;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.R;

import java.util.ArrayList;
import java.util.List;

import Datos.AdminSQLiteOpenHelper;
import Datos.CursosDao;
import Datos.HorarioDao;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class Fragment_Horarios_Admin extends Fragment {

    private Spinner spinCurso, spinProfesor, spinDia;
    private EditText etMateria, etHoraInicio, etHoraFin;
    private Button btnGuardar;
    private CursosDao cursoDao;
    private HorarioDao horarioDao;
    private AdminSQLiteOpenHelper helper;

    private final String[] dias = {"Seleccionar", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};

    public Fragment_Horarios_Admin() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horarios_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinCurso = view.findViewById(R.id.spinCursoHorario);
        spinProfesor = view.findViewById(R.id.spinProfesorHorario);
        spinDia = view.findViewById(R.id.spinDiaHorario);
        etMateria = view.findViewById(R.id.etMateriaHorario);
        etHoraInicio = view.findViewById(R.id.etHoraInicioHorario);
        etHoraFin = view.findViewById(R.id.etHoraFinHorario);
        btnGuardar = view.findViewById(R.id.btnGuardarHorario);

        cursoDao = new CursosDao(requireContext());
        horarioDao = new HorarioDao(requireContext());
        helper = new AdminSQLiteOpenHelper(requireContext(), "BD_Sygemy", null, 1);

        cargarSpinnerCursos();
        cargarSpinnerProfesores();
        cargarSpinnerDias();

        btnGuardar.setOnClickListener(v -> guardarHorario());
    }

    private void cargarSpinnerCursos() {
        List<String> opciones = new ArrayList<>();
        opciones.add("Seleccionar");
        opciones.addAll(cursoDao.obtenerNombresCursos());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCurso.setAdapter(adapter);
    }

    private void cargarSpinnerProfesores() {
        List<HorarioDao.Profesor> profesores = horarioDao.obtenerProfesores();
        List<HorarioDao.Profesor> opciones = new ArrayList<>();
        opciones.add(new HorarioDao.Profesor(-1, "Seleccionar"));
        opciones.addAll(profesores);

        ArrayAdapter<HorarioDao.Profesor> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinProfesor.setAdapter(adapter);
    }

    private void cargarSpinnerDias() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDia.setAdapter(adapter);
    }

    private void guardarHorario() {
        if (spinCurso.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Seleccione un curso", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinProfesor.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Seleccione un profesor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinDia.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Seleccione un dia", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etMateria.getText().toString().trim().isEmpty()) {
            etMateria.setError("Ingrese la materia");
            return;
        }
        if (etHoraInicio.getText().toString().trim().isEmpty()) {
            etHoraInicio.setError("Ingrese hora de inicio");
            return;
        }
        if (etHoraFin.getText().toString().trim().isEmpty()) {
            etHoraFin.setError("Ingrese hora de fin");
            return;
        }

        String curso = spinCurso.getSelectedItem().toString();
        HorarioDao.Profesor profesor = (HorarioDao.Profesor) spinProfesor.getSelectedItem();
        String dia = spinDia.getSelectedItem().toString();
        String materia = etMateria.getText().toString().trim();
        String horaInicio = etHoraInicio.getText().toString().trim();
        String horaFin = etHoraFin.getText().toString().trim();

        SQLiteDatabase bd = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("curso_hor", curso);
        cv.put("materia_hor", materia);
        cv.put("dia_hor", dia);
        cv.put("horaInicio_hor", horaInicio);
        cv.put("horaFin_hor", horaFin);
        cv.put("profesor_id", profesor.id);

        long resultado = bd.insert("horarios", null, cv);

        if (resultado != -1) {
            Toast.makeText(requireContext(), "Horario guardado correctamente", Toast.LENGTH_SHORT).show();
            etMateria.setText("");
            etHoraInicio.setText("");
            etHoraFin.setText("");
            spinCurso.setSelection(0);
            spinProfesor.setSelection(0);
            spinDia.setSelection(0);
        } else {
            Toast.makeText(requireContext(), "Error al guardar el horario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (helper != null) helper.close();
    }
}
