package Datos;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Base64;

import Entidades.Horarios;
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

        BaseDeDatos.execSQL("CREATE TABLE cursos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE)");

        String[] cursosIniciales = {"1er Grado", "2do Grado", "3er Grado", "4to Grado", "5to Grado", "6to Grado"};
        for (String c : cursosIniciales) {
            ContentValues cv = new ContentValues();
            cv.put("nombre", c);
            BaseDeDatos.insert("cursos", null, cv);
        }

        BaseDeDatos.execSQL("CREATE TABLE profesores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni_prof TEXT UNIQUE NOT NULL," +
                "nombre_prof TEXT NOT NULL," +
                "apellido_prof TEXT NOT NULL," +
                "genero_prof TEXT," +
                "materia_prof TEXT," +
                "usuario_id INTEGER," +
                "sueldo_prof REAL," +
                "foto_prof TEXT," +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");

        BaseDeDatos.execSQL("CREATE TABLE alumnos ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni_alum TEXT UNIQUE NOT NULL," +
                "nombre_alum TEXT NOT NULL," +
                "apellido_alum TEXT NOT NULL," +
                "curso_alum TEXT NOT NULL," +
                "genero_alum TEXT," +
                "foto_alum TEXT," +
                "usuario_id INTEGER," +
                "cuotas_pagadas INTEGER DEFAULT 0,"+
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
                "profesor_id INTEGER," +
                "descripcion TEXT," +
                "nota REAL NOT NULL," +
                "fecha TEXT," +
                "materia_cal TEXT," +
                "entrega_id INTEGER," +
                "FOREIGN KEY(alumno_id) REFERENCES alumnos(id)," +
                "FOREIGN KEY(entrega_id) REFERENCES entregas(id_ent)," +
                "FOREIGN KEY(trimestre_id) REFERENCES trimestres(id)," +
                "FOREIGN KEY(profesor_id) REFERENCES profesores(id))");

        BaseDeDatos.execSQL("CREATE TABLE actividad (" +
                "id_act INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo_act TEXT NOT NULL," +
                "titulo_act TEXT NOT NULL," +
                "descripcion_act TEXT," +
                "fecha_act TEXT," +
                "url_act TEXT," +
                "archivo_nombre TEXT," +
                "archivo_url TEXT," +
                "materia_act TEXT,"+
                "trimestre_id INTEGER," +
                "profesor_id INTEGER," +
                "FOREIGN KEY(trimestre_id) REFERENCES trimestres(id)," +
                "FOREIGN KEY(profesor_id) REFERENCES profesores(id))");

        BaseDeDatos.execSQL("CREATE TABLE entregas (" +
                "id_ent INTEGER PRIMARY KEY AUTOINCREMENT," +
                "actividad_id INTEGER NOT NULL," +
                "alumno_id INTEGER NOT NULL," +
                "url_ent TEXT," +
                "archivo_nombre TEXT," +
                "archivo_url TEXT," +
                "fecha_ent TEXT," +
                "comentario TEXT," +
                "FOREIGN KEY(actividad_id) REFERENCES actividad(id_act)," +
                "FOREIGN KEY(alumno_id) REFERENCES alumnos(id))");

        BaseDeDatos.execSQL("CREATE TABLE horarios (" +
                "id_hor INTEGER PRIMARY KEY AUTOINCREMENT," +
                "curso_hor TEXT NOT NULL," +
                "materia_hor TEXT NOT NULL," +
                "dia_hor TEXT NOT NULL," +
                "horaInicio_hor TEXT NOT NULL," +
                "horaFin_hor TEXT NOT NULL," +
                "profesor_id INTEGER," +
                "FOREIGN KEY(profesor_id) REFERENCES profesores(id))");

        ContentValues profeValores = new ContentValues();
        profeValores.put("dni_prof", "21642241");
        profeValores.put("nombre_prof", "Sebastian");
        profeValores.put("apellido_prof", "Herrera");
        profeValores.put("materia_prof", "Biología");
        profeValores.put("genero_prof","Masculino");
        profeValores.put("sueldo_prof","213000.1312");
        long profesorId = BaseDeDatos.insert("profesores", null, profeValores);
        ContentValues usuarios = new ContentValues();
        usuarios.put("usuario","sebah");
        usuarios.put("contrasenia","sebah");
        usuarios.put("rol","profesor");
        BaseDeDatos.insert("usuarios",null, usuarios);

        Horarios hora = new Horarios("5to Grado","Matematica","Lunes","09:00","11:00", (int) profesorId);
        BaseDeDatos.insert("horarios",null,hora.Valores());

        BaseDeDatos.execSQL("CREATE TABLE materias_grado (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "curso TEXT NOT NULL," +
                "materia TEXT NOT NULL," +
                "UNIQUE(curso, materia))");

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
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS actividad");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS entregas");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS horarios");
        BaseDeDatos.execSQL("DROP TABLE IF EXISTS cursos");

        onCreate(BaseDeDatos);
    }
}
