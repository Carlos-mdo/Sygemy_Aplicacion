package Ventana.Admin;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.MainActivity;
import com.aplicacion.gestion_escolar.MenuAdminActivity;
import com.aplicacion.gestion_escolar.R;

import Datos.AdminSQLiteOpenHelper;

public class Fragment_Profesores extends Fragment {

    private EditText etNombre,etApellido,etUsuario, etContrasenia, etDni, etSueldo;
    public String[] generos = {"Seleccionar","Femenino","Masculino"};

    public String[] materias = {
            "Seleccionar",
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
    public Spinner spinnerGenero, spinnerMateria;
    public TextView txtErrorGenero;
    public Button btnGuardar;
    public ImageButton btnRegreso;
    public ContentValues contentValues = new ContentValues();
    public ContentValues contentValuesProfe = new ContentValues();
    public  AdminSQLiteOpenHelper datos;
    public SQLiteDatabase baseDeDatos;
    public boolean estado;

    public Fragment_Profesores() {
    }

    public static Fragment_Profesores newInstance(String param1, String param2) {
        Fragment_Profesores fragment = new Fragment_Profesores();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__profesores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datos = new AdminSQLiteOpenHelper(requireContext(), "BD_Sygemy", null, 1);
        baseDeDatos = datos.getWritableDatabase();

        etNombre = view.findViewById(R.id.etNombreProfe);
        etApellido = view.findViewById(R.id.etApellidoProfe);
        etUsuario = view.findViewById(R.id.etUsuarioProfe);
        etContrasenia = view.findViewById(R.id.etContraseniaProfe);
        etDni = view.findViewById(R.id.etDniProfe);
        etSueldo = view.findViewById(R.id.etSueldoProfe);
        spinnerGenero = view.findViewById(R.id.spinGenero);
        spinnerMateria = view.findViewById(R.id.spinMateria);

        ArrayAdapter<String> array = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,generos);

        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGenero.setAdapter(array);

        ArrayAdapter<String> adapterMateria = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, materias);

        adapterMateria.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerMateria.setAdapter(adapterMateria);

        btnGuardar = view.findViewById(R.id.btnAgregarProfe);
        btnRegreso = view.findViewById(R.id.btnRegresoProfe);

        //btnGuardar.setOnClickListener(view1 -> guardarProfesor());

        btnRegreso.setOnClickListener(v -> {
            baseDeDatos.close();
            Intent intent = new Intent(requireContext(), MenuAdminActivity.class);
            startActivity(intent);
        });

        btnGuardar.setOnClickListener(view1 -> {
            if(ValidacionProfesores()){
                guardarProfesor();
            }
        });

    }
    public void guardarProfesor() {

        contentValues.clear();
        contentValuesProfe.clear();

        contentValues.put("usuario", etUsuario.getText().toString());
        contentValues.put("contrasenia", etContrasenia.getText().toString());
        contentValues.put("rol", "profesor");

        long idUsuario = baseDeDatos.insert("usuarios",null,contentValues);

        if (idUsuario == -1) {
            Toast.makeText(getContext(),
                    "El usuario ya existe",
                    Toast.LENGTH_SHORT).show();

            etUsuario.setError("Usuario ya registrado");
            return;
        }



        contentValuesProfe.put("dni_prof",etDni.getText().toString());
        contentValuesProfe.put("nombre_prof",etNombre.getText().toString());
        contentValuesProfe.put("apellido_prof",etApellido.getText().toString());
        contentValuesProfe.put("materia_prof",spinnerMateria.getSelectedItem().toString());
        contentValuesProfe.put("genero_prof", spinnerGenero.getSelectedItem().toString());
        contentValuesProfe.put("sueldo_prof", Double.parseDouble(etSueldo.getText().toString()));
        contentValuesProfe.put("usuario_id", idUsuario);

        baseDeDatos.insert("profesores",null,contentValuesProfe);

        etNombre.setText("");
        etApellido.setText("");
        etUsuario.setText("");
        etContrasenia.setText("");
        etDni.setText("");
        etSueldo.setText("");
        spinnerMateria.setSelection(0);
        spinnerGenero.setSelection(0);

    }

    public boolean ValidacionProfesores(){
        estado = true;

        etDni.setError(null);
        etNombre.setError(null);
        etApellido.setError(null);
        etUsuario.setError(null);
        etContrasenia.setError(null);

        if(etDni.getText().toString().trim().isEmpty()){
            etDni.setError("Ingrese el DNI");
            estado = false;
        }
        if(etNombre.getText().toString().trim().isEmpty()){
            etNombre.setError("Ingrese el nombre del profesor");
            estado = false;
        }
        if(etApellido.getText().toString().trim().isEmpty()){
            etApellido.setError("Ingrese el apellido del profesor");
            estado = false;
        }
        if(etUsuario.getText().toString().trim().isEmpty()){
            etUsuario.setError("Ingrese el usuario del profesor");
            estado = false;
        }
        if(etContrasenia.getText().toString().trim().isEmpty()){
            etContrasenia.setError("Ingrese la contraseña del profesor");
            estado = false;
        }
        if(spinnerGenero.getSelectedItemPosition() == 0){
            Toast.makeText(getContext(), "Seleccione un género", Toast.LENGTH_SHORT).show();
            estado = false;
        }

        if (spinnerMateria.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(),
                    "Seleccione una materia",
                    Toast.LENGTH_SHORT).show();
            estado = false;
        }

        if (etSueldo.getText().toString().trim().isEmpty()) {
            etSueldo.setError("Ingrese el sueldo");
            estado = false;
        }

        return estado;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (baseDeDatos !=null && baseDeDatos.isOpen()){
            baseDeDatos.close();
        }
    }
}