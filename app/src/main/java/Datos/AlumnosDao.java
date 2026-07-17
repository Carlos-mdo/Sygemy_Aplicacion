package Datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entidades.Alumno;

public class AlumnosDao {

    private AdminSQLiteOpenHelper dbHelper;

    public AlumnosDao(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
    }

    public List<Alumno> obtenerAlumnosPorCurso(String curso) {
        List<Alumno> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT id, dni_alum, nombre_alum, apellido_alum, genero_alum, curso_alum, usuario_id " +
                        "FROM alumnos WHERE curso_alum = ? ORDER BY apellido_alum",
                new String[]{curso})) {

            while (cursor.moveToNext()) {
                Alumno alumno = new Alumno(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dni_alum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre_alum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("apellido_alum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("genero_alum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("curso_alum")),
                        cursor.isNull(cursor.getColumnIndexOrThrow("usuario_id")) ? -1 : cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                        cursor.isNull(cursor.getColumnIndexOrThrow("cuotas_pagadas")) ? 0 : cursor.getInt(cursor.getColumnIndexOrThrow("cuotas_pagadas"))
                );
                lista.add(alumno);
            }
        }
        return lista;
    }

    public int contarAlumnosPorCurso(String curso) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int cantidad = 0;
        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM alumnos WHERE curso_alum = ?",
                new String[]{curso})) {
            if (cursor.moveToFirst()) {
                cantidad = cursor.getInt(0);
            }
        }
        return cantidad;
    }

    public void cerrar() {
        dbHelper.close();
    }
}
