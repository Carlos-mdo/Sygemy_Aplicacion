package Ventana.Alumno;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Adapter.HoraAdapter;
import Datos.HorarioDao;
import Datos.SesionDao;
import Entidades.Horarios;

public class Fragment_Horarios extends Fragment {

    private RecyclerView recyclerHorarios;
    private HoraAdapter adapter;
    private HorarioDao horarioDAO;

    public Fragment_Horarios() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__horarios, container, false);

        recyclerHorarios = view.findViewById(R.id.recyclerHorarios);
        recyclerHorarios.setLayoutManager(new LinearLayoutManager(getContext()));

        horarioDAO = new HorarioDao(getContext());
        cargarHorarios();

        return view;
    }

    private void cargarHorarios() {
        int usuarioId = SesionDao.getInstancia().getUsuarioId();

        if (usuarioId == -1) {
            Toast.makeText(getContext(), "No se encontró la sesión del alumno", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Horarios> listas = horarioDAO.obtenerHorariosPorAlumno(usuarioId);

        android.util.Log.d("Fragment_Horarios", "usuarioId=" + usuarioId + " filas=" + listas.size());

        String curso = horarioDAO.obtenerCursoDeAlumno(usuarioId);
        if (curso == null) {
            Toast.makeText(getContext(), "No se encontró el curso del alumno", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Horarios> lista = horarioDAO.obtenerHorariosPorCurso(curso);

        if (lista.isEmpty()) {
            Toast.makeText(getContext(), "No hay horarios cargados para tu curso", Toast.LENGTH_SHORT).show();
        }

        adapter = new HoraAdapter(lista);
        recyclerHorarios.setAdapter(adapter);
    }
}