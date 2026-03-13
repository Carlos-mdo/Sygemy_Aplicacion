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
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.MainActivity;
import com.aplicacion.gestion_escolar.R;

import Datos.AdminSQLiteOpenHelper;

public class Fragment_Profesores extends Fragment {

    private EditText etNombre,etApellido,etUsuario, etContrasenia, etDni, etMateria;
    public String[] generos = {"Seleccionar","Femenino","Masculino"};
    public Spinner spinnerGenero;
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
        etMateria = view.findViewById(R.id.etMateriaProfe);
        spinnerGenero = view.findViewById(R.id.spinGenero);

        ArrayAdapter<String> array = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,generos);

        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGenero.setAdapter(array);

        btnGuardar = view.findViewById(R.id.btnAgregarProfe);
        btnRegreso = view.findViewById(R.id.btnRegresoProfe);

        btnGuardar.setOnClickListener(view1 -> guardarProfesor());

        Intent intent = new Intent(requireContext(), MainActivity.class);

        btnRegreso.setOnClickListener(view1 -> startActivity(intent));

//        if(!ValidacionProfesores()){
//            return;
//        }else{
//            Toast.makeText(getContext(),"Complete todos los campos",Toast.LENGTH_SHORT).show();
//        }
    }

    public void guardarProfesor(){

        contentValues.clear();
        contentValuesProfe.clear();

        contentValues.put("usuario",etUsuario.getText().toString());
        contentValues.put("contrasenia",etContrasenia.getText().toString());
        contentValues.put("rol","profesor");

        long idUsuario = baseDeDatos.insert("usuarios",null,contentValues);

        baseDeDatos.insert("usuarios",null,contentValues);

        contentValuesProfe.put("dni_prof",etDni.getText().toString());
        contentValuesProfe.put("nombre_prof",etNombre.getText().toString());
        contentValuesProfe.put("apellido_prof",etApellido.getText().toString());
        contentValuesProfe.put("materia",etMateria.getText().toString());
        contentValuesProfe.put("genero_prof", spinnerGenero.getSelectedItem().toString());
        contentValuesProfe.put("usuario_id", idUsuario);

        baseDeDatos.insert("profesores",null,contentValuesProfe);

        etNombre.setText("");
        etApellido.setText("");
        etUsuario.setText("");
        etContrasenia.setText("");
        etDni.setText("");
        etMateria.setText("");
        spinnerGenero.setSelection(0);

        baseDeDatos.close();

    }

//    public boolean ValidacionProfesores(){
//        estado = true;
//
//        etDni.setError(null);
//        etNombre.setError(null);
//        etApellido.setError(null);
//        etUsuario.setError(null);
//        etContrasenia.setError(null);
//
//        if(etDni.getText().toString().isEmpty()){
//            etDni.setError("Ingrese el DNI");
//            estado = false;
//        }
//        if(etNombre.getText().toString().isEmpty()){
//            etNombre.setError("Ingrese el nombre del profesor");
//            estado = false;
//        }
//        if(etApellido.getText().toString().isEmpty()){
//            etApellido.setError("Ingrese el apellido del profesor");
//            estado = false;
//        }
//        if(etUsuario.getText().toString().isEmpty()){
//            etUsuario.setError("Ingrese el usuario del profesor");
//            estado = false;
//        }
//        if(etContrasenia.getText().toString().isEmpty()){
//            etContrasenia.setError("Ingrese la contraseña del profesor");
//            estado = false;
//        }
//        if(spinnerGenero.getSelectedItemPosition() == 0){
//           txtErrorGenero.setError("Ingrese un genero");
//            estado = false;
//        }
//
//
//        return estado;
//    }

}