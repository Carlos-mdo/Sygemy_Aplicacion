package Datos;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Base64;

import Entidades.Usuarios;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {

        BaseDeDatos.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT NOT NULL UNIQUE," +
                "contrasenia TEXT NOT NULL," +
                "rol TEXT NOT NULL)");

        Usuarios admin = new Usuarios("admin","admin123","admin");
        BaseDeDatos.insert("usuarios",null,admin.Valores());

        BaseDeDatos.execSQL("CREATE TABLE profesores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni_prof TEXT UNIQUE NOT NULL," +
                "nombre_prof TEXT NOT NULL," +
                "apellido_prof TEXT NOT NULL," +
                "genero_prof TEXT," + // todavia no implementado en la plantilla profesores
                "materia TEXT," +
                "usuario_id INTEGER," +
                "sueldo_id MONEY," +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");

        BaseDeDatos.execSQL("CREATE TABLE alumnos ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni_alum TEXT UNIQUE NOT NULL," +
                "nombre_alum TEXT NOT NULL," +
                "apellido_alum TEXT NOT NULL," +
                "curso_alum TEXT NOT NULL," +
                "genero_alum TEXT," +
                "usuario_id INTEGER," +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");

        BaseDeDatos.execSQL("CREATE TABLE trimestres (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "fecha_inicio TEXT," +
                "fecha_fin TEXT)");

        BaseDeDatos.execSQL("CREATE TABLE calificaciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "alumno_id INTEGER NOT NULL," +
                "trimestre_id INTEGER NOT NULL," +
                "descripcion TEXT," +
                "nota REAL NOT NULL," +
                "fecha TEXT," +
                "FOREIGN KEY(alumno_id) REFERENCES alumnos(id)," +
                "FOREIGN KEY(trimestre_id) REFERENCES trimestres(id))");

        BaseDeDatos.execSQL("INSERT INTO trimestres (nombre) VALUES ('1° Trimestre')");
        BaseDeDatos.execSQL("INSERT INTO trimestres (nombre) VALUES ('2° Trimestre')");
        BaseDeDatos.execSQL("INSERT INTO trimestres (nombre) VALUES ('3° Trimestre')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase BaseDeDatos, int i, int i1) {
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS usuarios");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS profesores");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS alumnos");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS calificaciones");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS trimestres");
        onCreate(BaseDeDatos);
    }
}
