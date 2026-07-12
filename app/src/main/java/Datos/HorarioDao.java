package Datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entidades.Horarios;
public class HorarioDao {
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;

    public HorarioDao(Context context) {
        admin = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
    }

    public List<Horarios> obtenerHorariosPorCurso(String curso) {
        List<Horarios> lista = new ArrayList<>();
        bd = admin.getReadableDatabase();

        String query = "SELECT h.id_hor, h.curso_hor, h.materia_hor, h.dia_hor, " +
                "h.horaInicio_hor, h.horaFin_hor, h.profesor_id, " +
                "(p.nombre_prof || ' ' || p.apellido_prof) AS docente " +
                "FROM horarios h " +
                "LEFT JOIN profesores p ON h.profesor_id = p.id " +
                "WHERE h.curso_hor = ? " +
                "ORDER BY h.dia_hor, h.horaInicio_hor";

        try (Cursor cursor = bd.rawQuery(query, new String[]{curso})) {
            if (cursor.moveToFirst()) {
                do {
                    int profesorId = cursor.isNull(6) ? -1 : cursor.getInt(6);
                    String docente = cursor.isNull(7) ? "Sin asignar" : cursor.getString(7);

                    Horarios horario = new Horarios(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            profesorId,
                            docente
                    );
                    lista.add(horario);
                } while (cursor.moveToNext());
            }
        }
        bd.close();
        return lista;
    }

    public String obtenerCursoDeAlumno(int usuarioId) {
        bd = admin.getReadableDatabase();
        String curso = null;

        try (Cursor cursor = bd.rawQuery(
                "SELECT curso_alum FROM alumnos WHERE usuario_id = ?",
                new String[]{String.valueOf(usuarioId)})) {
            if (cursor.moveToFirst()) {
                curso = cursor.getString(0);
            }
        }
        bd.close();
        return curso;
    }

    // Lista de profesores (id + nombre completo), útil para un spinner al crear/editar un horario
    public List<Profesor> obtenerProfesores() {
        List<Profesor> lista = new ArrayList<>();
        bd = admin.getReadableDatabase();

        try (Cursor cursor = bd.rawQuery(
                "SELECT id, (nombre_prof || ' ' || apellido_prof) AS nombre FROM profesores ORDER BY apellido_prof", null)) {
            if (cursor.moveToFirst()) {
                do {
                    lista.add(new Profesor(cursor.getInt(0), cursor.getString(1)));
                } while (cursor.moveToNext());
            }
        }
        bd.close();
        return lista;
    }

    public static class Profesor {
        public final int id;
        public final String nombre;

        public Profesor(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() { return nombre; } // para usar directo en un ArrayAdapter<Profesor>
    }
}
