package Servicios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Datos.AdminSQLiteOpenHelper;
import Entidades.Usuarios;
import Entidades.Profesor;

public class ServicioAdmin {
        protected Context context;
        protected boolean estado;
        private AdminSQLiteOpenHelper admin;
        private SQLiteDatabase base_Datos;

        public ServicioAdmin(Context context){
            this.context = context;
            admin = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
            base_Datos = admin.getWritableDatabase();
        }

        public boolean buscarAdmin(String etUsuario, String etContrasenia) {

        Cursor cursor = base_Datos.rawQuery(
                "SELECT * FROM usuarios WHERE usuario=? AND contrasenia=?",
                new String[]{etUsuario, etContrasenia}
        );

        boolean existe = cursor.moveToFirst();

        cursor.close();

        return existe;
        }
    public List<Usuarios> BuscarTodosUsuarios() {

        List<Usuarios> listaUsuarios = new ArrayList<>();

        Cursor cursor = base_Datos.rawQuery("SELECT * FROM usuarios", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
                String contrasenia = cursor.getString(cursor.getColumnIndexOrThrow("contrasenia"));
                String rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"));

                Usuarios usuario = new Usuarios(id,nombreUsuario, contrasenia, rol);
                listaUsuarios.add(usuario);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return listaUsuarios;
    }

    public List<Profesor> BuscarTodosProfesores() {

        List<Profesor> listaProfesores = new ArrayList<>();

        Cursor cursor = base_Datos.rawQuery("SELECT * FROM profesores", null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id"));

                String dni = cursor.getString(
                        cursor.getColumnIndexOrThrow("dni_prof"));

                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre_prof"));

                String apellido = cursor.getString(
                        cursor.getColumnIndexOrThrow("apellido_prof"));

                String genero = cursor.getString(
                        cursor.getColumnIndexOrThrow("genero_prof"));

                String materia = cursor.getString(
                        cursor.getColumnIndexOrThrow("materia"));

                int usuarioId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("usuario_id"));

                Profesor profesor = new Profesor(
                        id,
                        dni,
                        nombre,
                        apellido,
                        genero,
                        materia,
                        usuarioId
                );

                listaProfesores.add(profesor);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return listaProfesores;
    }
    public void eliminarUsuario(int id) {
        base_Datos.delete("usuarios", "id = ?",
                new String[]{String.valueOf(id)});
        base_Datos.delete("profesores", "usuario_id = ?",
                new String[]{String.valueOf(id)});
        base_Datos.delete("alumnos", "usuario_id = ?",
                new String[]{String.valueOf(id)});
    }

    public void actualizarUsuario(Usuarios usuario) {

        ContentValues valores = new ContentValues();
        valores.put("usuario", usuario.getUsuario());
        valores.put("contrasenia", usuario.getContrasenia());
        valores.put("rol", usuario.getRol());

        base_Datos.update("usuarios", valores, "id = ?",
                new String[]{String.valueOf(usuario.getId())});
    }
}
