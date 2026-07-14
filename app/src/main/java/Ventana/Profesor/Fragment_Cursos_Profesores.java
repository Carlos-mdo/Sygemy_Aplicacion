package Ventana.Profesor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aplicacion.gestion_escolar.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapter.CursosAdapter;
import Datos.CursosDao;
import Entidades.Cursos;

public class Fragment_Cursos_Profesores extends Fragment {

    private RecyclerView rvCursosProfesor;
    private TextView tvSinCursos, tvTituloCursos, tvMateriaProfesor, tvCantidadCursos;
    private TextInputEditText etSearchCurso;
    private CursosAdapter adapter;
    private CursosDao cursosDAO;
    private List<Cursos> listaCompleta = new ArrayList<>();

    public Fragment_Cursos_Profesores() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__cursos__profesores, container, false);
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCursosProfesor = view.findViewById(R.id.rvCursosProfesor);
        tvSinCursos = view.findViewById(R.id.tvSinCursos);
        tvTituloCursos = view.findViewById(R.id.tvTituloCursos);
        tvMateriaProfesor = view.findViewById(R.id.tvMateriaProfesor);
        tvCantidadCursos = view.findViewById(R.id.tvCantidadCursos);
        etSearchCurso = view.findViewById(R.id.etSearchCurso);

        rvCursosProfesor.setLayoutManager(new LinearLayoutManager(getContext()));

        cursosDAO = new CursosDao(getContext());
        tvTituloCursos.setText("Mis cursos");

        cargarCursosDelProfesor();
        configurarBuscador();
    }

    private void cargarCursosDelProfesor() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId == -1) {
            mostrarEstadoVacio();
            return;
        }

        int profesorId = cursosDAO.obtenerProfesorIdPorUsuario(usuarioId);
        if (profesorId == -1) {
            mostrarEstadoVacio();
            return;
        }

        String[] datosProfesor = cursosDAO.obtenerDatosProfesor(profesorId);
        tvMateriaProfesor.setText("Materia: " + datosProfesor[1]);

        listaCompleta = cursosDAO.obtenerCursosPorProfesor(profesorId);
        tvCantidadCursos.setText(listaCompleta.size() + (listaCompleta.size() == 1 ? " curso asignado" : " cursos asignados"));

        if (listaCompleta.isEmpty()) {
            mostrarEstadoVacio();
        } else {
            rvCursosProfesor.setVisibility(View.VISIBLE);
            tvSinCursos.setVisibility(View.GONE);
            adapter = new CursosAdapter(listaCompleta);
            rvCursosProfesor.setAdapter(adapter);
        }
    }
    private void configurarBuscador() {
        etSearchCurso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarCursos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filtrarCursos(String textoBusqueda) {
        if (listaCompleta.isEmpty()) return;

        List<Cursos> filtrada = new ArrayList<>();
        String texto = textoBusqueda.toLowerCase(Locale.getDefault()).trim();

        for (Cursos c : listaCompleta) {
            if (c.getNombreCurso().toLowerCase(Locale.getDefault()).contains(texto)
                    || c.getMateria().toLowerCase(Locale.getDefault()).contains(texto)) {
                filtrada.add(c);
            }
        }

        if (adapter != null) {
            adapter.actualizarLista(filtrada);
        }

        boolean vacio = filtrada.isEmpty();
        rvCursosProfesor.setVisibility(vacio ? View.GONE : View.VISIBLE);
        tvSinCursos.setVisibility(vacio ? View.VISIBLE : View.GONE);
        if (vacio) {
            tvSinCursos.setText("No se encontraron cursos con ese nombre");
        }
    }

    private void mostrarEstadoVacio() {
        rvCursosProfesor.setVisibility(View.GONE);
        tvSinCursos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cursosDAO != null) {
            cursosDAO.cerrar();
        }
    }
}