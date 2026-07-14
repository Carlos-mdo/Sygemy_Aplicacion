package Ventana.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.aplicacion.gestion_escolar.MenuAdminActivity;
import com.aplicacion.gestion_escolar.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AlumnoCursoAdapter;
import Adapter.HoraAdapter;
import Datos.AlumnosDao;
import Datos.CursosDao;
import Datos.HorarioDao;
import Entidades.Alumno;
import Entidades.Horarios;

public class Fragment_DetalleCursos extends Fragment {

    private Spinner spinCurso;
    private RecyclerView rvHorarios, rvAlumnos;
    private TextView tvSinDatos;
    private ImageButton btnRegreso;

    private CursosDao cursosDao;
    private HorarioDao horarioDao;
    private AlumnosDao alumnosDao;
    private HoraAdapter horaAdapter;
    private AlumnoCursoAdapter alumnoCursoAdapter;

    public Fragment_DetalleCursos() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_curso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinCurso = view.findViewById(R.id.spinCursoDetalle);
        rvHorarios = view.findViewById(R.id.rvHorariosDetalle);
        rvAlumnos = view.findViewById(R.id.rvAlumnosDetalle);
        tvSinDatos = view.findViewById(R.id.tvSinDatosDetalle);
        btnRegreso = view.findViewById(R.id.btnRegresoDetalleCurso);

        cursosDao = new CursosDao(requireContext());
        horarioDao = new HorarioDao(requireContext());
        alumnosDao = new AlumnosDao(requireContext());

        rvHorarios.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarSpinnerCursos();

        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MenuAdminActivity.class);
            startActivity(intent);
        });
    }

    private void cargarSpinnerCursos() {
        List<String> opciones = new ArrayList<>();
        opciones.add("Seleccionar");
        opciones.addAll(cursosDao.obtenerNombresCursos());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCurso.setAdapter(adapter);

        spinCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mostrarVacio();
                } else {
                    String cursoSeleccionado = opciones.get(position);
                    cargarDetalleCurso(cursoSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarDetalleCurso(String curso) {
        List<Horarios> horarios = horarioDao.obtenerHorariosPorCurso(curso);
        List<Alumno> alumnos = alumnosDao.obtenerAlumnosPorCurso(curso);

        boolean vacio = horarios.isEmpty() && alumnos.isEmpty();
        tvSinDatos.setVisibility(vacio ? View.VISIBLE : View.GONE);
        rvHorarios.setVisibility(vacio ? View.GONE : View.VISIBLE);
        rvAlumnos.setVisibility(vacio ? View.GONE : View.VISIBLE);

        if (horaAdapter == null) {
            horaAdapter = new HoraAdapter(horarios);
            rvHorarios.setAdapter(horaAdapter);
        } else {
            horaAdapter.actualizarLista(horarios);
        }

        if (alumnoCursoAdapter == null) {
            alumnoCursoAdapter = new AlumnoCursoAdapter(alumnos);
            rvAlumnos.setAdapter(alumnoCursoAdapter);
        } else {
            alumnoCursoAdapter.actualizarLista(alumnos);
        }
    }

    private void mostrarVacio() {
        rvHorarios.setVisibility(View.GONE);
        rvAlumnos.setVisibility(View.GONE);
        tvSinDatos.setVisibility(View.VISIBLE);
        tvSinDatos.setText("Elegí un curso para ver el detalle");
    }
}
