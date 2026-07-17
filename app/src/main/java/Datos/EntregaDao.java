package Datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entidades.Entrega;
import Entidades.EntregaPendiente;

public class EntregaDao {
    private final AdminSQLiteOpenHelper baseDeDatos;

    public EntregaDao(Context context) {
        baseDeDatos = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
    }

    public boolean insertar(Entrega entrega) {
        SQLiteDatabase bd = baseDeDatos.getWritableDatabase();
        long result= bd.insert("entregas", null, entrega.Valores());
        bd.close();
        return result != -1;
    }

    public boolean yaEntrego(int actividadId, int alumnoId) {
        SQLiteDatabase bd = baseDeDatos.getReadableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT id_ent FROM entregas WHERE actividad_id = ? AND alumno_id = ?", new String[]{ String.valueOf(actividadId), String.valueOf(alumnoId) }
        );
        boolean estado = fila.getCount() > 0;
        fila.close();
        bd.close();
        return estado;
    }
    public List<EntregaPendiente> obtenerPendientes(int profesorId) {
        List<EntregaPendiente> lista = new ArrayList<>();
        SQLiteDatabase bd_pendientes = baseDeDatos.getReadableDatabase();

        String sql = "SELECT ent.id_ent, ent.actividad_id, ent.alumno_id, act.titulo_act, " +
                "alum.apellido_alum || ' ' || alum.nombre_alum, alum.curso_alum, ent.url_ent, " +
                "ent.archivo_nombre, ent.archivo_url, ent.fecha_ent, ent.comentario " +
                "FROM entregas ent " +
                "INNER JOIN actividad act ON act.id_act = ent.actividad_id " +
                "INNER JOIN alumnos alum ON alum.id = ent.alumno_id " +
                "WHERE act.profesor_id = ? " +                                          // antes: act.materia_act = ?
                "AND ent.id_ent NOT IN (SELECT entrega_id FROM calificaciones WHERE entrega_id IS NOT NULL) " +
                "ORDER BY ent.fecha_ent DESC";

        Cursor fila = bd_pendientes.rawQuery(sql, new String[]{String.valueOf(profesorId)});

        while (fila.moveToNext()) {
            EntregaPendiente ent_pendiente = new EntregaPendiente(
                    fila.getInt(0),
                    fila.getInt(1),
                    fila.getInt(2),
                    fila.getString(3),
                    fila.getString(4),
                    fila.getString(5),
                    fila.getString(6),
                    fila.getString(7),
                    fila.getString(8),
                    fila.getString(9),
                    fila.getString(10)
            );
            lista.add(ent_pendiente);
        }
        fila.close();
        bd_pendientes.close();

        return lista;
    }

    public List<EntregaPendiente> obtenerCorregidas(int profesorId) {
        List<EntregaPendiente> lista = new ArrayList<EntregaPendiente>();
        SQLiteDatabase bd_corregidas = baseDeDatos.getReadableDatabase();

        String sql = "SELECT ent.id_ent, ent.actividad_id, ent.alumno_id, act.titulo_act, " +
                "alum.apellido_alum || ' ' || alum.nombre_alum, alum.curso_alum, ent.url_ent, " +
                "ent.archivo_nombre, ent.archivo_url, ent.fecha_ent, ent.comentario, cal.nota " +
                "FROM entregas ent " +
                "INNER JOIN actividad act ON act.id_act = ent.actividad_id " +
                "INNER JOIN alumnos alum ON alum.id = ent.alumno_id " +
                "INNER JOIN calificaciones cal ON cal.entrega_id = ent.id_ent " +
                "WHERE act.profesor_id = ? " +
                "ORDER BY ent.fecha_ent DESC";

        Cursor fila = bd_corregidas.rawQuery(sql, new String[]{String.valueOf(profesorId)});

        if (fila.moveToFirst()) {
            do {
                EntregaPendiente ent = new EntregaPendiente(
                        fila.getInt(0), fila.getInt(1), fila.getInt(2), fila.getString(3),
                        fila.getString(4), fila.getString(5), fila.getString(6), fila.getString(7),
                        fila.getString(8), fila.getString(9), fila.getString(10)
                );
                double notaAux = fila.getDouble(11);
                ent.nota = notaAux;
                ent.corregida = true;
                lista.add(ent);
            } while (fila.moveToNext());
        }
        fila.close();
        bd_corregidas.close();

        return lista;
    }

    public List<EntregaPendiente> obtenerPendientesAlumno(String materia, int alumn_id) {
        List<EntregaPendiente> lista = new ArrayList<>();
        SQLiteDatabase bd_pendienteAlumno = baseDeDatos.getReadableDatabase();
        String idAlumnoStr = String.valueOf(alumn_id);

        String sql = "SELECT ent.id_ent, ent.actividad_id, ent.alumno_id, act.titulo_act, " +
                "alum.apellido_alum || ' ' || alum.nombre_alum, alum.curso_alum, ent.url_ent, " +
                "ent.archivo_nombre, ent.archivo_url, ent.fecha_ent, ent.comentario, act.trimestre_id, trimes.nombre " +
                "FROM entregas ent " +
                "INNER JOIN actividad act ON act.id_act = ent.actividad_id " +
                "INNER JOIN alumnos alum ON alum.id = ent.alumno_id " +
                "INNER JOIN trimestres trimes ON trimes.id = act.trimestre_id " +
                "WHERE act.materia_act = ? AND ent.alumno_id = ? " +
                "AND ent.id_ent NOT IN (SELECT entrega_id FROM calificaciones WHERE entrega_id IS NOT NULL) " +
                "ORDER BY ent.fecha_ent DESC";

        Cursor fila = bd_pendienteAlumno.rawQuery(sql, new String[]{materia, idAlumnoStr});

        if (fila != null && fila.moveToFirst()) {
            do {
                EntregaPendiente ent_pendientes = new EntregaPendiente(
                        fila.getInt(0), fila.getInt(1), fila.getInt(2), fila.getString(3),
                        fila.getString(4), fila.getString(5), fila.getString(6), fila.getString(7),
                        fila.getString(8), fila.getString(9), fila.getString(10)
                );

                ent_pendientes.trimestreId = fila.getInt(11);
                ent_pendientes.trimestreNombre = fila.getString(12);

                lista.add(ent_pendientes);

            } while (fila.moveToNext());
        }
        if (fila != null) { fila.close(); }
        bd_pendienteAlumno.close();

        return lista;
    }
}
