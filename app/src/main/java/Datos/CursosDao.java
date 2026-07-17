package Datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Entidades.Alumno;
import Entidades.CursoConMaterias;
import Entidades.Cursos;

public class CursosDao {

    private AdminSQLiteOpenHelper dbHelper;

    private Context context;
    private  MateriaGradoDao materiaGradoDao;

    public CursosDao(Context context) {
        this.context = context;
        dbHelper = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
        materiaGradoDao = new MateriaGradoDao(context);
    }
    public int obtenerProfesorIdPorUsuario(int usuarioId) {
        int profesorId = -1;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM profesores WHERE usuario_id = ?",
                new String[]{String.valueOf(usuarioId)}
        );
        if (cursor.moveToFirst()) {
            profesorId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return profesorId;
    }
    public List<String> obtenerNombresCursos() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase bd = dbHelper.getReadableDatabase();
        try (Cursor cursor = bd.rawQuery("SELECT nombre FROM cursos ORDER BY nombre", null)) {
            while (cursor.moveToNext()) {
                lista.add(cursor.getString(0));
            }
        }
        return lista;
    }
    public List<Alumno> obtenerAlumnosPorProfesor(int profesorId, AlumnosDao alumnosDao) {
        List<Alumno> resultado = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT curso_hor FROM horarios WHERE profesor_id = ?",
                new String[]{String.valueOf(profesorId)}
        );

        List<String> cursos = new ArrayList<>();
        while (cursor.moveToNext()) {
            cursos.add(cursor.getString(0));
        }
        cursor.close();

        for (String curso : cursos) {
            resultado.addAll(alumnosDao.obtenerAlumnosPorCurso(curso));
        }
        return resultado;
    }
    public String[] obtenerDatosProfesor(int profesorId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT nombre_prof, apellido_prof, materia_prof FROM profesores WHERE id = ?",
                new String[]{String.valueOf(profesorId)}
        );
        String[] datos = new String[]{"", ""};
        if (cursor.moveToFirst()) {
            String nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombre_prof"))
                    + " " + cursor.getString(cursor.getColumnIndexOrThrow("apellido_prof"));
            String materia = cursor.getString(cursor.getColumnIndexOrThrow("materia_prof"));
            datos[0] = nombreCompleto;
            datos[1] = materia;
        }
        cursor.close();
        return datos;
    }
    public List<Cursos> obtenerCursosPorProfesor(int profesorId) {

        Map<String, List<String>> diasPorCurso = new LinkedHashMap<>();
        Map<String, String> horaInicioPorCurso = new LinkedHashMap<>();
        Map<String, String> horaFinPorCurso = new LinkedHashMap<>();
        Map<String, String> materiaPorCurso = new LinkedHashMap<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT curso_hor, materia_hor, dia_hor, horaInicio_hor, horaFin_hor " +
                        "FROM horarios WHERE profesor_id = ? ORDER BY curso_hor",
                new String[]{String.valueOf(profesorId)}
        );

        while (cursor.moveToNext()) {
            String curso = cursor.getString(cursor.getColumnIndexOrThrow("curso_hor"));
            String materia = cursor.getString(cursor.getColumnIndexOrThrow("materia_hor"));
            String dia = cursor.getString(cursor.getColumnIndexOrThrow("dia_hor"));
            String horaInicio = cursor.getString(cursor.getColumnIndexOrThrow("horaInicio_hor"));
            String horaFin = cursor.getString(cursor.getColumnIndexOrThrow("horaFin_hor"));

            String clave = curso + "|" + materia;

            if (!diasPorCurso.containsKey(clave)) {
                diasPorCurso.put(clave, new ArrayList<>());
                horaInicioPorCurso.put(clave, horaInicio);
                horaFinPorCurso.put(clave, horaFin);
                materiaPorCurso.put(clave, materia);
            }
            diasPorCurso.get(clave).add(dia);
        }
        cursor.close();

        List<Cursos> resultado = new ArrayList<>();
        for (String clave : diasPorCurso.keySet()) {
            String nombreCurso = clave.split("\\|")[0];
            String dias = String.join(", ", diasPorCurso.get(clave));
            int cantidadAlumnos = contarAlumnosDeCurso(db, nombreCurso);

            resultado.add(new Cursos(
                    nombreCurso,
                    materiaPorCurso.get(clave),
                    dias,
                    horaInicioPorCurso.get(clave),
                    horaFinPorCurso.get(clave),
                    cantidadAlumnos
            ));
        }
        return resultado;
    }
    public List<CursoConMaterias> obtenerCursosConMateriasPorProfesor(int profesorId) {
        List<Cursos> propios = obtenerCursosPorProfesor(profesorId); // uno por curso+materia

        Map<String, List<Cursos>> porCurso = new LinkedHashMap<>();
        for (Cursos c : propios) {
            porCurso.computeIfAbsent(c.getNombreCurso(), k -> new ArrayList<>()).add(c);
        }

        List<CursoConMaterias> resultado = new ArrayList<>();
        for (Map.Entry<String, List<Cursos>> entry : porCurso.entrySet()) {
            String nombreCurso = entry.getKey();
            List<Cursos> combos = entry.getValue();

            List<String> materiasDelProfesor = new ArrayList<>();
            StringBuilder horarioResumen = new StringBuilder();
            for (Cursos c : combos) {
                materiasDelProfesor.add(c.getMateria());
                if (horarioResumen.length() > 0) horarioResumen.append(" | ");
                horarioResumen.append(c.getMateria()).append(": ").append(c.getDias())
                        .append(" ").append(c.getHoraInicio()).append("-").append(c.getHoraFin());
            }

            List<String> materiasDelGrado = materiaGradoDao.obtenerMateriasDeCurso(nombreCurso);
            if (materiasDelGrado.isEmpty()) {
                // Currícula todavía no cargada para este curso: al menos mostramos lo que dicta el profesor
                materiasDelGrado = materiasDelProfesor;
            }

            int cantidadAlumnos = combos.get(0).getCantidadAlumnos();

            resultado.add(new CursoConMaterias(
                    nombreCurso, cantidadAlumnos, materiasDelGrado, materiasDelProfesor, horarioResumen.toString()));
        }
        return resultado;
    }
    private int contarAlumnosDeCurso(SQLiteDatabase db, String nombreCurso) {
        int cantidad = 0;
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM alumnos WHERE curso_alum = ?",
                new String[]{nombreCurso}
        );
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }
        cursor.close();
        return cantidad;
    }
    public void cerrar() {
        dbHelper.close();
        if (materiaGradoDao != null) {
            materiaGradoDao.cerrar();
        }
    }
}