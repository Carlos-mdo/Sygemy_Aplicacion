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
import Entidades.Alumno;
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

        Cursor fila = base_Datos.rawQuery("SELECT * FROM usuarios WHERE usuario=? AND contrasenia=?", new String[]{etUsuario, etContrasenia});

        boolean existe = fila.moveToFirst();
        fila.close();

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

        Cursor fila_profe = base_Datos.rawQuery("SELECT * FROM profesores", null);

        if (fila_profe.moveToFirst()) {
            do {
                int id = fila_profe.getInt(fila_profe.getColumnIndexOrThrow("id"));

                String dni = fila_profe.getString(fila_profe.getColumnIndexOrThrow("dni_prof"));

                String nombre = fila_profe.getString(fila_profe.getColumnIndexOrThrow("nombre_prof"));

                String apellido = fila_profe.getString(fila_profe.getColumnIndexOrThrow("apellido_prof"));

                String genero = fila_profe.getString(fila_profe.getColumnIndexOrThrow("genero_prof"));

                String materia = fila_profe.getString(fila_profe.getColumnIndexOrThrow("materia"));

                int usuarioId = fila_profe.getInt(fila_profe.getColumnIndexOrThrow("usuario_id"));

                double sueldo = fila_profe.getDouble(fila_profe.getColumnIndexOrThrow("sueldo_prof"));

                Profesor profesor = new Profesor(id, dni, nombre, apellido, genero, materia, usuarioId, sueldo);

                listaProfesores.add(profesor);

            } while (fila_profe.moveToNext());
        }
        fila_profe.close();

        return listaProfesores;
    }
    public List<Alumno> BuscarTodosAlumnos() {

        List<Alumno> listaAlumnos = new ArrayList<>();

        Cursor fila_alum = base_Datos.rawQuery("SELECT * FROM alumnos", null);

        if (fila_alum.moveToFirst()) {
            do {
                int id = fila_alum.getInt(fila_alum.getColumnIndexOrThrow("id"));

                String dni = fila_alum.getString(fila_alum.getColumnIndexOrThrow("dni_alum"));

                String nombre = fila_alum.getString(fila_alum.getColumnIndexOrThrow("nombre_alum"));

                String apellido = fila_alum.getString(fila_alum.getColumnIndexOrThrow("apellido_alum"));

                String genero = fila_alum.getString(fila_alum.getColumnIndexOrThrow("genero_alum"));

                String materia = fila_alum.getString(fila_alum.getColumnIndexOrThrow("curso_alum"));

                int usuarioId = fila_alum.getInt(fila_alum.getColumnIndexOrThrow("usuario_id"));

                Alumno alumno = new Alumno(id, dni, nombre, apellido, genero, materia, usuarioId);
                listaAlumnos.add(alumno);

            } while (fila_alum.moveToNext());
        }
        fila_alum.close();

        return listaAlumnos;
    }
    public void eliminarUsuario(int id) {
        base_Datos.delete("usuarios", "id = ?", new String[]{String.valueOf(id)});
        base_Datos.delete("profesores", "usuario_id = ?", new String[]{String.valueOf(id)});
        base_Datos.delete("alumnos", "usuario_id = ?", new String[]{String.valueOf(id)});
    }

    public void actualizarUsuario(Usuarios usuario) {

        ContentValues valores = new ContentValues();
        valores.put("usuario", usuario.getUsuario());
        valores.put("contrasenia", usuario.getContrasenia());
        valores.put("rol", usuario.getRol());

        base_Datos.update("usuarios", valores, "id = ?", new String[]{String.valueOf(usuario.getId())});
    }
    public void actualizarProfesor(Profesor profesor) {

        ContentValues valores = new ContentValues();

        valores.put("dni_prof", profesor.getDni());
        valores.put("nombre_prof", profesor.getNombre());
        valores.put("apellido_prof", profesor.getApellido());
        valores.put("genero_prof", profesor.getGenero());
        valores.put("materia_prof", profesor.getMateria());
        valores.put("sueldo_prof", profesor.getSueldo());

        base_Datos.update("profesores", valores, "id = ?", new String[]{String.valueOf(profesor.getId())}
        );
    }


    public void actualizarAlumno(Alumno alumno) {

        ContentValues valores = new ContentValues();
        valores.put("dni_alum", alumno.getDni());
        valores.put("nombre_alum", alumno.getNombre());
        valores.put("apellido_alum", alumno.getApellido());
        valores.put("curso_alum", alumno.getCurso());
        valores.put("genero_alum", alumno.getGenero());

        base_Datos.update("alumnos", valores, "id = ?", new String[]{String.valueOf(alumno.getId())});
    }
    public boolean existeDni(String dni) {

        Cursor cursor = base_Datos.rawQuery(
                "SELECT 1 FROM alumnos WHERE dni_alum = ? " +
                        "UNION " +
                        "SELECT 1 FROM profesores WHERE dni_prof = ?",
                new String[]{dni, dni}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();

        return existe;
    }

    public boolean existeUsuario(String usuario) {

        Cursor cursor = base_Datos.rawQuery(
                "SELECT id FROM usuarios WHERE usuario = ?",
                new String[]{usuario}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();

        return existe;
    }

    public void actualizarCuotas(int idAlumno, int cuotas) {

        ContentValues valores = new ContentValues();
        valores.put("cuotasPagadas_alum", cuotas);

        base_Datos.update(
                "alumnos",
                valores,
                "id = ?",
                new String[]{String.valueOf(idAlumno)}
        );
    }
}
