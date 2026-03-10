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
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");

        BaseDeDatos.execSQL("CREATE TABLE alumnos ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni_alum TEXT UNIQUE NOT NULL," +
                "nombre_alum TEXT NOT NULL," +
                "apellido_alum TEXT NOT NULL," +
                "genero_alum TEXT," +
                "usuario_id INTEGER," +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase BaseDeDatos, int i, int i1) {
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS usuarios");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS profesores");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS alumnos");
        onCreate(BaseDeDatos);
    }
}
