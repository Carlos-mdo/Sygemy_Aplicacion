package com.aplicacion.gestion_escolar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Entidades.Usuarios;
import Servicios.ServicioAdmin;
import Servicios.ServicioUsuario;

public class MainActivity extends AppCompatActivity {

    protected EditText etUsuario, etContrasenia;
    ServicioUsuario servUsuario;
    public Usuarios rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        servUsuario = new ServicioUsuario(this);
        etUsuario = findViewById(R.id.editTextUsuario);
        etContrasenia = findViewById(R.id.editTextContrasenia);
    }

    public boolean validaciones () {

        boolean estado = true;

        etUsuario.setError(null);
        etContrasenia.setError(null);

        if (etUsuario.getText().toString().isEmpty()) {
            etUsuario.setError("Ingrese Usuario");
            estado = false;
        }
        if(etContrasenia.getText().toString().isEmpty()){
            etContrasenia.setError("Ingrese la contraseña");
            estado = false;
        }

        return estado;
    }

    public void btnIniciar (View view){

        if(!validaciones()){
            return;
        }

        rol = servUsuario.buscarUsuario(etUsuario.getText().toString(),etContrasenia.getText().toString());

        if(rol!=null){

            Datos.SesionDao.getInstancia().iniciarSesion(rol);
            String rolTexto = rol.getRol();

            if (rolTexto.equals("admin")) {
                guardarRol("admin");
                Intent intent = new Intent(this, MenuAdminActivity.class);
                startActivity(intent);
            } else if (rolTexto.equals("profesor")) {
                Datos.CursosDao cursosDao = new Datos.CursosDao(this);
                int profesorId = cursosDao.obtenerProfesorIdPorUsuario(rol.getId());
                String materia = cursosDao.obtenerDatosProfesor(profesorId)[1];
                cursosDao.cerrar();

                Datos.UsuarioDao.guardarProfesor(this, profesorId, materia);
                guardarRol("profe");
                Intent intent = new Intent(this, MenuUsuarioActivity.class);
                startActivity(intent);
            } else if (rolTexto.equals("alumno")) {
                int alumnoId = obtenerAlumnoIdPorUsuario(rol.getId());
                Datos.UsuarioDao.guardarAlumno(this, alumnoId);
                guardarRol("alumn");

                Intent intent = new Intent(this, MenuUsuarioActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "error al buscar el usuario", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(this,"Ningun dato fue recibido",Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarRol(String rol){
        SharedPreferences shPref = getSharedPreferences("Roles", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = shPref.edit();
        edit.clear();
        edit.putString("rol", rol);
        edit.apply();
    }

    private int obtenerAlumnoIdPorUsuario(int usuarioId) {
        Datos.AdminSQLiteOpenHelper admin = new Datos.AdminSQLiteOpenHelper(this, "BD_Sygemy", null, 1);
        android.database.sqlite.SQLiteDatabase db = admin.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery("SELECT id FROM alumnos WHERE usuario_id = ?", new String[]{ String.valueOf(usuarioId) });
        int id = -1;
        if (cursor.moveToFirst()) { id = cursor.getInt(0); }
        cursor.close();
        db.close();
        return id;
    }

}