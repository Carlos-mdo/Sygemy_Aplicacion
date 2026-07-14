package Ventana.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.MainActivity;
import com.aplicacion.gestion_escolar.MenuAdminActivity;
import com.aplicacion.gestion_escolar.R;

import Datos.AdminSQLiteOpenHelper;
import Servicios.ServicioAdmin;

public class Fragment_Alumnos extends Fragment {

    public String[] generos = {"Seleccionar", "Femenino", "Masculino"};
    public String[] curso = {"Seleccionar", "1", "2", "3","4","5","6"};
    public Spinner spinnerGenero, spinnerCurso;
    public EditText etDni, etNombre, etApellido, etUsuario, etContrasenia;
    public Button btnGuardado;
    public ImageButton btnRegreso;
    private ServicioAdmin servicio;

    protected AdminSQLiteOpenHelper datos;
    protected SQLiteDatabase baseDeDatos;

    public Fragment_Alumnos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__alumnos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datos = new AdminSQLiteOpenHelper(requireContext(), "BD_Sygemy", null, 1);
        baseDeDatos = datos.getWritableDatabase();

        servicio = new ServicioAdmin(requireContext());

        etDni        = view.findViewById(R.id.etDniAlum);
        etNombre     = view.findViewById(R.id.etNombreAlum);
        etApellido   = view.findViewById(R.id.etApellidoAlum);
        etUsuario    = view.findViewById(R.id.etUsuarioAlum);
        etContrasenia = view.findViewById(R.id.etContraseniaAlum);
        spinnerCurso = view.findViewById(R.id.spinCurso);
        spinnerGenero= view.findViewById(R.id.spinGenero);
        btnGuardado  = view.findViewById(R.id.btnAgregarAlum);
        btnRegreso   = view.findViewById(R.id.btnRegresoAlum);

        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                generos
        );

        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGenero);

        ArrayAdapter<String> adapterCurso = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                curso
        );

        adapterCurso.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCurso.setAdapter(adapterCurso);

        btnGuardado.setOnClickListener(v -> {
            if (validacionAlumnos()) {
                guardarAlumno();
            }
        });

        btnRegreso.setOnClickListener(v -> {
            baseDeDatos.close();
            Intent intent = new Intent(requireContext(), MenuAdminActivity.class);
            startActivity(intent);
        });
    }

    public void guardarAlumno() {

        ContentValues valorUsuario = new ContentValues();
        valorUsuario.put("usuario", etUsuario.getText().toString().trim());
        valorUsuario.put("contrasenia",etContrasenia.getText().toString().trim());
        valorUsuario.put("rol", "alumno");

        long idUsuario = baseDeDatos.insert("usuarios", null, valorUsuario);

        ContentValues valorAlumno = new ContentValues();
        valorAlumno.put("dni_alum",    etDni.getText().toString().trim());
        valorAlumno.put("nombre_alum", etNombre.getText().toString().trim());
        valorAlumno.put("apellido_alum",etApellido.getText().toString().trim());
        valorAlumno.put("curso_alum",  spinnerCurso.getSelectedItem().toString());
        valorAlumno.put("genero_alum", spinnerGenero.getSelectedItem().toString());
        valorAlumno.put("usuario_id",  idUsuario);
        valorAlumno.put("cuotasPagadas_alum", 0);

        long idAlumno = baseDeDatos.insert("alumnos", null, valorAlumno);

//        if (idAlumno == -1) {
//            Toast.makeText(getContext(),
//                    "El DNI ya está registrado", Toast.LENGTH_SHORT).show();
//            etDni.setError("DNI ya registrado");
//            baseDeDatos.delete("usuarios", "id = ?",
//                    new String[]{ String.valueOf(idUsuario) });
//            return;
//        }

        Toast.makeText(getContext(), "Alumno guardado correctamente ✓", Toast.LENGTH_SHORT).show();

        etDni.setText("");
        etNombre.setText("");
        etApellido.setText("");
        etUsuario.setText("");
        etContrasenia.setText("");
        spinnerCurso.setSelection(0);
        spinnerGenero.setSelection(0);
    }

    public boolean validacionAlumnos() {

        boolean valido = true;

        etDni.setError(null);
        etNombre.setError(null);
        etApellido.setError(null);
        etUsuario.setError(null);
        etContrasenia.setError(null);


        if (etDni.getText().toString().trim().isEmpty()) {
            etDni.setError("Ingrese el DNI");
            valido = false;
        }

        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("Ingrese el nombre");
            valido = false;
        }

        if (etApellido.getText().toString().trim().isEmpty()) {
            etApellido.setError("Ingrese el apellido");
            valido = false;
        }

        if (etUsuario.getText().toString().trim().isEmpty()) {
            etUsuario.setError("Ingrese el usuario");
            valido = false;
        }

        if (etContrasenia.getText().toString().trim().isEmpty()) {
            etContrasenia.setError("Ingrese la contraseña");
            valido = false;
        }

        if (spinnerGenero.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Seleccione un género", Toast.LENGTH_SHORT).show();
            valido = false;
        }

        if (spinnerCurso.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Seleccione un curso", Toast.LENGTH_SHORT).show();
            valido = false;
        }

        if (servicio.existeDni(etDni.getText().toString().trim())) {
            etDni.setError("Ese DNI ya está registrado");
            valido = false;
        }

        if (servicio.existeUsuario(etUsuario.getText().toString().trim())) {
            etUsuario.setError("Ese usuario ya existe");
            valido = false;
        }

        String dni = etDni.getText().toString().trim();

        if (!dni.matches("\\d{8}")) {
            etDni.setError("El DNI debe tener 8 números");
            valido = false;
        }

        String nombre = etNombre.getText().toString().trim();

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            etNombre.setError("El nombre solo puede contener letras");
            valido = false;
        }

        String apellido = etApellido.getText().toString().trim();

        if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            etApellido.setError("El apellido solo puede contener letras");
            valido = false;
        }

        String usuario = etUsuario.getText().toString().trim();

        if (usuario.contains(" ")) {
            etUsuario.setError("El usuario no puede contener espacios");
            valido = false;
        }


//    if (etContrasenia.getText().toString().length() < 6) {
//        etContrasenia.setError("La contraseña debe tener al menos 6 caracteres");
//        valido = false;
//    }

        return valido;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (baseDeDatos != null && baseDeDatos.isOpen()) {
            baseDeDatos.close();
        }
    }
}