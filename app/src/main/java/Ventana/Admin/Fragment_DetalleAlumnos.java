package Ventana.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.MenuAdminActivity;
import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Adapter.AlumnoAdapter;
import Adapter.ProfesorAdapter;
import Entidades.Alumno;
import Entidades.Profesor;
import Servicios.ServicioAdmin;

public class Fragment_DetalleAlumnos extends Fragment {

    private ServicioAdmin servicioAdmin;
    private List<Alumno> listAlumnos;
    private RecyclerView recyclerAlumnos;
    private ImageButton btnRegreso;

    public Fragment_DetalleAlumnos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment__detalleusuarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        servicioAdmin = new ServicioAdmin(getContext());

        listAlumnos = servicioAdmin.BuscarTodosAlumnos();

        recyclerAlumnos = view.findViewById(R.id.recyclerUsuarios);

        recyclerAlumnos.setLayoutManager(new GridLayoutManager(getContext(), 2));

        AlumnoAdapter adapter = new AlumnoAdapter(listAlumnos,servicioAdmin);

        recyclerAlumnos.setAdapter(adapter);

        btnRegreso = view.findViewById(R.id.btnRegresoDetalles);

        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MenuAdminActivity.class);
            startActivity(intent);
        });
    }
}
