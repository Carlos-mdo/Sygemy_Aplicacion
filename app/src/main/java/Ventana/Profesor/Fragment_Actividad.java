package Ventana.Profesor;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.FragmentBase;
import com.aplicacion.gestion_escolar.R;

public class Fragment_Actividad extends FragmentBase {

    public Fragment_Actividad() {}
    private Button btnDesplegar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment__actividad, container, false);

        btnDesplegar = view.findViewById(R.id.btnAgregarAct);
        btnDesplegar.setOnClickListener(view1 -> mostrarMenu(view1));
        vincularBotonMenu(view, R.id.iBtnMenu);
        return view;
    }

    private void mostrarMenu(View view) {
        PopupMenu popOpciones = new PopupMenu(requireContext(), view);
        popOpciones.getMenu().add(0, 1, 0, "Tarea");
        popOpciones.getMenu().add(0, 2, 0, "Examen");
        popOpciones.getMenu().add(0, 3, 0, "Trabajo Práctico");

        popOpciones.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    mostrarMensaje("Tarea");
                    return true;
                case 2:
                    mostrarMensaje("Examen");
                    return true;
                case 3:
                    mostrarMensaje("Trabajo Práctico");
                    return true;
                default:
                    return false;
            }
        });

        popOpciones.show();
    }

    private void mostrarMensaje(String tipo) {
        View ventFlotante = LayoutInflater.from(requireContext()).inflate(R.layout.view_mensaje, null);

        EditText etTitulo      = ventFlotante.findViewById(R.id.etTitulo);
        EditText etDescripcion = ventFlotante.findViewById(R.id.etDescripcion);
        EditText etFecha       = ventFlotante.findViewById(R.id.etFecha);

        new AlertDialog.Builder(requireContext()).setTitle("Agregar " + tipo).setView(ventFlotante)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String titulo      = etTitulo.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    String fecha       = etFecha.getText().toString().trim();

                    if (titulo.isEmpty()) {
                        Toast.makeText(requireContext(),"Agregue un titulo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(descripcion.isEmpty()){
                        Toast.makeText(requireContext(),"Agregue la descripcion", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(fecha.isEmpty()){
                        Toast.makeText(requireContext(),"Agregue la fecha", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(requireContext(),tipo + " guardado: " + titulo, Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancelar", null).show();
    }
}