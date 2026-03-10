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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.MainActivity;
import com.aplicacion.gestion_escolar.R;

import Datos.AdminSQLiteOpenHelper;

public class Fragment_Profesores extends Fragment {

    private EditText etNombre,etApellido,etUsuario, etContrasenia, etDni, etMateria;
    public Button btnGuardar;
    public ImageButton btnRegreso;
    public ContentValues contentValues = new ContentValues();
    public  AdminSQLiteOpenHelper datos;
    public SQLiteDatabase baseDeDatos;

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

        btnGuardar = view.findViewById(R.id.btnAgregarProfe);
        btnRegreso = view.findViewById(R.id.btnRegresoProfe);

        btnGuardar.setOnClickListener(view1 -> guardarProfesor());

        Intent intent = new Intent(requireContext(), MainActivity.class);

        btnRegreso.setOnClickListener(view1 -> startActivity(intent));

    }

    public void guardarProfesor(){

        contentValues.put("usuario",etUsuario.getText().toString());
        contentValues.put("contrasenia",etContrasenia.getText().toString());
        contentValues.put("rol","profesor");

        baseDeDatos.insert("usuarios",null,contentValues);

        contentValues.put("dni_prof",etDni.getText().toString());
        contentValues.put("nombre_prof",etNombre.getText().toString());
        contentValues.put("apellido_prof",etApellido.getText().toString());
        contentValues.put("materia",etMateria.getText().toString());

        baseDeDatos.insert("profesores",null,contentValues);

        etNombre.setText("");
        etApellido.setText("");
        etUsuario.setText("");
        etContrasenia.setText("");
        etDni.setText("");
        etMateria.setText("");

        baseDeDatos.close();

    }

}