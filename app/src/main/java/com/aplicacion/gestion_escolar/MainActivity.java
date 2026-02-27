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

import java.sql.SQLData;
import java.time.Instant;

import Datos.AdminSQLiteOpenHelper;
import Entidades.Usuarios;
import Servicios.ServicioAdmin;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasenia;
    public TextView txt_UsuarioRol;
    ServicioAdmin servAdmin;

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
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
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
            return ;
        }

        if(servAdmin.buscarAdmin(etUsuario,etContrasenia)){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this,"error al buscar",Toast.LENGTH_SHORT).show();
        }

        //iniciarSesion();
    }

}