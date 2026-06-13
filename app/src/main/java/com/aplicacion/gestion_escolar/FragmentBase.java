package com.aplicacion.gestion_escolar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public abstract class FragmentBase extends Fragment {

    protected ControllerDrawerMenu menuControlador;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ControllerDrawerMenu) {
            menuControlador = (ControllerDrawerMenu) context;
        }
    }
    public void vincularBotonMenu(View view, int btnId) {
        ImageButton btnMenu = view.findViewById(btnId);
        if (btnMenu != null && menuControlador !=null) {
            btnMenu.setOnClickListener(v -> menuControlador.abrirNav());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        menuControlador = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }
}