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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import Entidades.Profesor;
import Servicios.ServicioAdmin;

public class ProfesorAdapter extends RecyclerView.Adapter<ProfesorAdapter.ViewHolder> {

    private List<Profesor> listaProfesores;
    private ServicioAdmin servicio;

    public ProfesorAdapter(List<Profesor> listaProfesores, ServicioAdmin servicio) {
        this.listaProfesores = listaProfesores;
        this.servicio = servicio;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profesor, parent, false);

        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Profesor profesor = listaProfesores.get(position);
        holder.txtNombre.setText(profesor.getNombre() + " " + profesor.getApellido());
        holder.txtMateria.setText(profesor.getMateria());
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        holder.btnEditar.setOnClickListener(v -> {

            Context context = v.getContext();
            mostrarDialogoEditarProfesores(context, profesor, holder);
        });
        holder.btnEliminar.setOnClickListener(v -> {

            Context context = v.getContext();


            new AlertDialog.Builder(context).setTitle("Eliminar").setMessage("¿Seguro que querés eliminar este usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        int pos = holder.getAdapterPosition();

                        servicio.eliminarUsuario(profesor.getUsuarioId());

                        listaProfesores.remove(pos);
                        notifyItemRemoved(pos);

                        Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null).show();
        });

        holder.btnInformacion.setOnClickListener(v -> {

            Context context = v.getContext();

            String info =
                    "ID: " + profesor.getId() + "\n\n" +
                            "DNI: " + profesor.getDni() + "\n\n" +
                            "Materia: " + profesor.getMateria() + "\n\n" +
                            "Genero: " + profesor.getGenero() + "\n\n" +
                            "Sueldo: " + formato.format(profesor.getSueldo()) + "\n\n" +
                            "ID Usuario: " + profesor.getUsuarioId();

            new AlertDialog.Builder(context).setTitle("Información del usuario").setMessage(info)
                    .setPositiveButton("Editar", (dialog, which) -> {

                        mostrarDialogoEditarProfesores(context, profesor, holder);

                    }).setNegativeButton("Cerrar", null).show();
        });
    }


    @Override
    public int getItemCount() {
        return listaProfesores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtMateria;
        ImageButton btnEditar,btnEliminar, btnInformacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtMateria = itemView.findViewById(R.id.txtMateria);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnInformacion = itemView.findViewById(R.id.btnInformacion);
        }
    }
    private void mostrarDialogoEditarProfesores(Context context, Profesor profesor, ViewHolder holder) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        EditText inputNombre = new EditText(context);
        inputNombre.setHint("Nombre");
        inputNombre.setText(profesor.getNombre());

        EditText inputApellido = new EditText(context);
        inputApellido.setHint("Apellido");
        inputApellido.setText(profesor.getApellido());

        EditText inputDni = new EditText(context);
        inputDni.setHint("DNI");
        inputDni.setText(profesor.getDni());

        EditText inputSueldo = new EditText(context);
        inputSueldo.setHint("Sueldo");
        inputSueldo.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputSueldo.setText(
                java.math.BigDecimal
                        .valueOf(profesor.getSueldo())
                        .stripTrailingZeros()
                        .toPlainString()
        );

        Spinner spinnerMateria = new Spinner(context);

        String[] materias = {
                "Matemática",
                "Lengua",
                "Historia",
                "Geografía",
                "Biología",
                "Física",
                "Química",
                "Inglés",
                "Educación Física",
                "Informática"
        };

        ArrayAdapter<String> adapterMateria = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                materias
        );

        adapterMateria.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerMateria.setAdapter(adapterMateria);

        for (int i = 0; i < materias.length; i++) {
            if (materias[i].equals(profesor.getMateria())) {
                spinnerMateria.setSelection(i);
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
            if (generos[i].equals(profesor.getGenero())) {
                spinnerGenero.setSelection(i);
                break;
            }
        }

        layout.addView(inputNombre);
        layout.addView(inputApellido);
        layout.addView(inputDni);
        layout.addView(spinnerMateria);
        layout.addView(inputSueldo);
        layout.addView(spinnerGenero);

        new AlertDialog.Builder(context)
                .setTitle("Editar profesor")
                .setView(layout)

                .setPositiveButton("Guardar", (dialog, which) -> {

                    String nombre = inputNombre.getText().toString().trim();
                    String apellido = inputApellido.getText().toString().trim();
                    String dni = inputDni.getText().toString().trim();
                    String sueldo = inputSueldo.getText().toString().trim();

                    if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || sueldo.isEmpty()) {
                        Toast.makeText(context,
                                "Complete todos los campos",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                        Toast.makeText(context,
                                "El nombre solo puede contener letras",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                        Toast.makeText(context,
                                "El apellido solo puede contener letras",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!dni.matches("\\d{8}")) {
                        Toast.makeText(context,
                                "El DNI debe tener 8 dígitos",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!dni.equals(profesor.getDni()) && servicio.existeDni(dni)) {
                        Toast.makeText(context,
                                "Ese DNI ya está registrado",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double sueldoValor;

                    try {
                        sueldoValor = Double.parseDouble(sueldo);

                        if (sueldoValor <= 0) {
                            Toast.makeText(context,
                                    "Ingrese un sueldo válido",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(context,
                                "Ingrese un sueldo válido",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    profesor.setNombre(inputNombre.getText().toString());
                    profesor.setApellido(inputApellido.getText().toString());
                    profesor.setDni(inputDni.getText().toString());
                    profesor.setMateria(spinnerMateria.getSelectedItem().toString());
                    profesor.setGenero(spinnerGenero.getSelectedItem().toString());
                    profesor.setSueldo(
                            Double.parseDouble(inputSueldo.getText().toString())
                    );

                    servicio.actualizarProfesor(profesor);

                    notifyItemChanged(holder.getAdapterPosition());

                    Toast.makeText(context,
                            "Profesor actualizado",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null).show();
    }
}