package Ventana.Alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Adapter.CalificaAdapter;
import Datos.CalificacionDao;
import Entidades.Calificacion;


public class Fragment_Notas extends Fragment {

    private int alumId = 1;
    private RecyclerView rvCalificacion_Nota;
    private TextView txtSinCalificacion;

    public Fragment_Notas() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__notas, container, false);
        rvCalificacion_Nota = view.findViewById(R.id.rvCalificacionAlumno);
        txtSinCalificacion = view.findViewById(R.id.txtSinCalificacion);
        rvCalificacion_Nota.setLayoutManager(new LinearLayoutManager(requireContext()));

        cargarCalificaciones();
        return view;
    }

    private void cargarCalificaciones() {
        CalificacionDao daoCalif = new CalificacionDao(requireContext());
        List<Calificacion> lista = daoCalif.obtenAlumno(alumId);

        if (lista.isEmpty()) {
            txtSinCalificacion.setVisibility(View.VISIBLE);
            rvCalificacion_Nota.setVisibility(View.GONE);
        } else {
            txtSinCalificacion.setVisibility(View.GONE);
            rvCalificacion_Nota.setVisibility(View.VISIBLE);
        }
        rvCalificacion_Nota.setAdapter(new CalificaAdapter(lista, requireContext()));
    }
}