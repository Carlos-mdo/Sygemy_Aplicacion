package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Alumno;
import Entidades.Profesor;
import Servicios.ServicioAdmin;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.ViewHolder> {

    private List<Alumno> listaAlumno;

    private ServicioAdmin servicio;
    public AlumnoAdapter(List<Alumno> listaAlumno, ServicioAdmin servicio) {

        this.listaAlumno = listaAlumno;
        this.servicio = servicio;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alumno, parent, false);

        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Alumno alumno = listaAlumno.get(position);

        holder.txtNombre.setText(
                alumno.getNombre() + " " + alumno.getApellido()
        );

        holder.txtCurso.setText(
                alumno.getCurso()
        );

        holder.btnEliminar.setOnClickListener(v -> {

            Context context = v.getContext();


            new AlertDialog.Builder(context)
                    .setTitle("Eliminar")
                    .setMessage("¿Seguro que querés eliminar este usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        int pos = holder.getAdapterPosition();

                        servicio.eliminarUsuario(alumno.getUsuarioId());

                        listaAlumno.remove(pos);
                        notifyItemRemoved(pos);

                        Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.btnInformacion.setOnClickListener(v -> {

            Context context = v.getContext();

            String info =
                    "ID: " + alumno.getId() + "\n\n" +
                            "DNI: " + alumno.getDni() + "\n\n" +
                            "Genero: " + alumno.getGenero() + "\n\n" +
                            "ID Usuario: " + alumno.getUsuarioId();

            new AlertDialog.Builder(context)
                    .setTitle("Información del usuario")
                    .setMessage(info)

                    .setPositiveButton("Editar", (dialog, which) -> {

                        mostrarDialogoEditarAlumnos(context, alumno, holder);

                    })

                    .setNegativeButton("Cerrar", null)
                    .show();
        });

        holder.btnEditar.setOnClickListener(v ->{

            Context context = v.getContext();

            mostrarDialogoEditarAlumnos(context, alumno, holder);
        });
    }

    @Override
    public int getItemCount() {
        return listaAlumno.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtCurso;
        ImageButton btnEliminar, btnInformacion, btnEditar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCurso = itemView.findViewById(R.id.txtCurso);

            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnInformacion = itemView.findViewById(R.id.btnInformacion);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
    private void mostrarDialogoEditarAlumnos(Context context,
                                                Alumno alumno,
                                                AlumnoAdapter.ViewHolder holder) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        EditText inputNombre = new EditText(context);
        inputNombre.setHint("Nombre");
        inputNombre.setText(alumno.getNombre());

        EditText inputApellido = new EditText(context);
        inputApellido.setHint("Apellido");
        inputApellido.setText(alumno.getApellido());

        EditText inputDni = new EditText(context);
        inputDni.setHint("DNI");
        inputDni.setText(alumno.getDni());

        Spinner spinnerCurso = new Spinner(context);

        String[] curso = {
                "1",
                "2",
                "3",
                "4",
                "5",
                "6"
        };

        ArrayAdapter<String> adapterCurso = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                curso
        );

        adapterCurso.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerCurso.setAdapter(adapterCurso);

        for (int i = 0; i < curso.length; i++) {
            if (curso[i].equals(alumno.getCurso())) {
                spinnerCurso.setSelection(i);
                break;
            }
        }

        Spinner spinnerGenero = new Spinner(context);

        String[] generos = {"Femenino", "Masculino"};

        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                generos
        );

        adapterGenero.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerGenero.setAdapter(adapterGenero);

        for (int i = 0; i < generos.length; i++) {
            if (generos[i].equals(alumno.getGenero())) {
                spinnerGenero.setSelection(i);
                break;
            }
        }

        layout.addView(inputNombre);
        layout.addView(inputApellido);
        layout.addView(inputDni);
        layout.addView(spinnerCurso);
        layout.addView(spinnerGenero);

        new AlertDialog.Builder(context)
                .setTitle("Editar alumno")
                .setView(layout)

                .setPositiveButton("Guardar", (dialog, which) -> {

                    if (inputNombre.getText().toString().trim().isEmpty() ||
                            inputApellido.getText().toString().trim().isEmpty() ||
                            inputDni.getText().toString().trim().isEmpty()){

                        Toast.makeText(context,
                                "Complete todos los campos",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    alumno.setNombre(inputNombre.getText().toString());
                    alumno.setApellido(inputApellido.getText().toString());
                    alumno.setDni(inputDni.getText().toString());
                    alumno.setCurso(spinnerCurso.getSelectedItem().toString());
                    alumno.setGenero(spinnerGenero.getSelectedItem().toString());


                    servicio.actualizarAlumno(alumno);

                    notifyItemChanged(holder.getAdapterPosition());

                    Toast.makeText(context,
                            "Alumno actualizado",
                            Toast.LENGTH_SHORT).show();
                })

                .setNegativeButton("Cancelar", null)
                .show();
    }
}