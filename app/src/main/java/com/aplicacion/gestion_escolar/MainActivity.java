package com.aplicacion.gestion_escolar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Datos.AdminSQLiteOpenHelper;
import Servicios.ServicioAdmin;
import Servicios.ServicioUsuario;

public class MainActivity extends AppCompatActivity {

    protected EditText etUsuario, etContrasenia;
    public TextView txt_UsuarioRol;
    ServicioAdmin servAdmin;
    ServicioUsuario servUsuario;
    public String rol;


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

        servAdmin = new ServicioAdmin(this);
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

    public void iniciarSesion(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_Sygemy", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String _usuario = etUsuario.getText().toString();
        String _contrasenia = etContrasenia.getText().toString();

        if(!_usuario.isEmpty() && ! _contrasenia.isEmpty()){
            ContentValues registrar = new ContentValues();

            registrar.put("usuario", _usuario);
            registrar.put("contrasenia", _contrasenia);

            baseDeDatos.insert("usuarios", null, registrar);

            baseDeDatos.close();

            etUsuario.setText("");
            etContrasenia.setText("");

        } else {
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnIniciar (View view){

        if(!validaciones()){
            return;
        }

        rol = servUsuario.buscarUsuario(etUsuario.getText().toString(),etContrasenia.getText().toString());

        if(rol!=null){

            if(rol.equals("admin")){
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }
            else if(rol.equals("profesor")){

                Intent intent = new Intent(this, ProfesorActivity.class);
                //intent.putExtra("ROL_USUARIO", rol);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this,"error al buscar el usuario",Toast.LENGTH_SHORT).show();
            }

        }else
        {
            Toast.makeText(this,"Ningun dato fue recibido",Toast.LENGTH_SHORT).show();
        }

        //iniciarSesion();
    }

}