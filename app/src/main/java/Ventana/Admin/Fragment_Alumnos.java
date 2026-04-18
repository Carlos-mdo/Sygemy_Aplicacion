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
import com.aplicacion.gestion_escolar.R;

import Datos.AdminSQLiteOpenHelper;

public class Fragment_Alumnos extends Fragment {

    public String[] generos = {"Seleccionar","Femenino","Masculino"};
    public Spinner spinnerGenero;
    public EditText etDni,etNombre,etApellido,etUsuario, etContrasenia;
    public Button btnGuardado;
    public ImageButton btnRegreso;
    protected AdminSQLiteOpenHelper datos;
    protected SQLiteDatabase baseDeDatos;
    protected ContentValues contentValues = new ContentValues();
    public Boolean estado;
    public ContentValues contentValuesEstudiantes = new ContentValues();

    public Fragment_Alumnos() {
    }
    public static Fragment_Alumnos newInstance(String param1, String param2) {
        Fragment_Alumnos fragment = new Fragment_Alumnos();
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
        return inflater.inflate(R.layout.fragment__alumnos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datos = new AdminSQLiteOpenHelper(requireContext(),"BD_Sygemy",null,1);
        baseDeDatos = datos.getWritableDatabase();

        etDni = view.findViewById(R.id.etDniAlum);
        etNombre = view.findViewById(R.id.etNombreAlum);
        etApellido = view.findViewById(R.id.etApellidoAlum);
        etUsuario = view.findViewById(R.id.etUsuarioAlum);
        etContrasenia = view.findViewById(R.id.etContraseniaAlum);
        spinnerGenero = view.findViewById(R.id.spinGenero);

        btnGuardado = view.findViewById(R.id.btnAgregarAlum);
        btnRegreso = view.findViewById(R.id.btnRegresoAlum);

        ArrayAdapter<String> array = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,generos);

        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGenero.setAdapter(array);

        btnGuardado.setOnClickListener(view1 -> guardarAlumno());
        Intent intent = new Intent(getContext(), MainActivity.class);

        btnRegreso.setOnClickListener(view1 -> startActivity(intent));

        if(!validacionAlumnos()){
            return;
        }else{
            Toast.makeText(getContext(),"Complete todos los campos",Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarAlumno(){

        contentValues.put("usuario",etUsuario.getText().toString());
        contentValues.put("contrasenia",etContrasenia.getText().toString());
        contentValues.put("rol","alumno");

        long idUsuario = baseDeDatos.insert("usuarios",null, contentValues);

        contentValuesEstudiantes.put("dni_alum",etDni.getText().toString());
        contentValuesEstudiantes.put("nombre_alum",etNombre.getText().toString());
        contentValuesEstudiantes.put("apellido_alum",etApellido.getText().toString());
        contentValuesEstudiantes.put("genero_alum",spinnerGenero.getSelectedItem().toString());

        // vincular alumno con usuario
        contentValuesEstudiantes.put("usuario_id",idUsuario);

        baseDeDatos.insert("alumnos",null, contentValuesEstudiantes);

        etDni.setText("");
        etNombre.setText("");
        etApellido.setText("");
        etUsuario.setText("");
        etContrasenia.setText("");
        spinnerGenero.setSelection(0);
        baseDeDatos.close();
    }

    public boolean validacionAlumnos(){
        estado = true;

        etDni.setError(null);
        etNombre.setError(null);
        etApellido.setError(null);
        etUsuario.setError(null);
        etContrasenia.setError(null);

        if(etDni.getText().toString().isEmpty()){
            etDni.setError("Ingrese el DNI");
            estado = false;
        }
        if(etNombre.getText().toString().isEmpty()){
            etNombre.setError("Ingrese el nombre del alumno");
            estado = false;
        }
        if(etApellido.getText().toString().isEmpty()){
            etApellido.setError("Ingrese el apellido del alumno");
            estado = false;
        }
        if(etUsuario.getText().toString().isEmpty()){
            etUsuario.setError("Ingrese el usuario del alumno");
            estado = false;
        }
        if(etContrasenia.getText().toString().isEmpty()){
            etContrasenia.setError("Ingrese la contraseña del alumno");
            estado = false;
        }
//        if(spinnerGenero.getSelectedItemPosition() == 0){
//            Toast.makeText(getContext(), "Seleccione una opcion", Toast.LENGTH_SHORT).show();
//            estado = false;
//        }
        //Actualmente no existe un if para verificar el contenido del Spinner debido a errores constantes.

        return estado;
    }

}