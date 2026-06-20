package Ventana.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.aplicacion.gestion_escolar.MenuAdminActivity;
import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Adapter.UsuarioAdapter;
import Entidades.Usuarios;
import Servicios.ServicioAdmin;

public class Fragment_DetalleUsuarios extends Fragment {

    private ServicioAdmin servicioAdmin;
    private List<Usuarios> listUsuarios;
    private RecyclerView recyclerUsuarios;
    private ImageButton btnRegreso;

    public Fragment_DetalleUsuarios() {
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

        listUsuarios = servicioAdmin.BuscarTodosUsuarios();

        recyclerUsuarios = view.findViewById(R.id.recyclerUsuarios);

        recyclerUsuarios.setLayoutManager(new GridLayoutManager(getContext(), 2));

        UsuarioAdapter adapter = new UsuarioAdapter(listUsuarios, servicioAdmin);

        recyclerUsuarios.setAdapter(adapter);

        btnRegreso = view.findViewById(R.id.btnRegresoDetalles);

        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MenuAdminActivity.class);
            startActivity(intent);
        });
    }
}