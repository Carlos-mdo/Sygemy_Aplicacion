package Ventana.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Adapter.UsuarioAdapter;
import Entidades.Usuarios;
import Servicios.ServicioAdmin;

public class Fragment_Detalles extends Fragment {

    private ServicioAdmin servicioAdmin;
    private List<Usuarios> listUsuarios;
    private RecyclerView recyclerUsuarios;

    public Fragment_Detalles() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment__detalles, container, false);
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
    }
}