package Ventana.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.aplicacion.gestion_escolar.MenuAdminActivity;
import com.aplicacion.gestion_escolar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapter.PagosAdapter;
import Entidades.Alumno;
import Servicios.ServicioAdmin;

public class Fragment_Pagos extends Fragment {

    private RecyclerView recyclerPagos;
    private PagosAdapter adapter;

    private ServicioAdmin servicio;

    private TextView txtFecha;
    private EditText etBuscar;
    private Spinner spinnerCurso;
    private ImageButton btnRegreso;

    public Fragment_Pagos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pagos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        servicio = new ServicioAdmin(requireContext());

        recyclerPagos = view.findViewById(R.id.rvPagos);
        txtFecha = view.findViewById(R.id.txtFecha);
        etBuscar = view.findViewById(R.id.etBuscarAlumno);
        spinnerCurso = view.findViewById(R.id.spinnerCurso);
        btnRegreso = view.findViewById(R.id.btnRegreso);

        recyclerPagos.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Alumno> lista = servicio.BuscarTodosAlumnos();

        adapter = new PagosAdapter(lista, servicio);
        recyclerPagos.setAdapter(adapter);

        String fecha = new SimpleDateFormat("MMMM yyyy", new Locale("es", "AR")).format(new Date());

        txtFecha.setText(fecha.substring(0,1).toUpperCase() + fecha.substring(1));

        String[] cursos = {
                "Todos",
                "1er Grado",
                "2do Grado",
                "3er Grado",
                "4to Grado",
                "5to Grado",
                "6to Grado"
        };

        ArrayAdapter<String> adapterCurso = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cursos);

        adapterCurso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCurso.setAdapter(adapterCurso);

        spinnerCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                adapter.filtrar(etBuscar.getText().toString(), spinnerCurso.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.filtrar(s.toString(), spinnerCurso.getSelectedItem().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MenuAdminActivity.class);
            startActivity(intent);
        });
    }
}
