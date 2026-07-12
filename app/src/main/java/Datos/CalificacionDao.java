package Datos;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entidades.Calificacion;
public class CalificacionDao {
    private final AdminSQLiteOpenHelper baseDeDatos;

    public CalificacionDao(Context context) {
        baseDeDatos = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
    }

    public List<Calificacion> obtenAlumno(int alumId) {
        List<Calificacion> lista = new ArrayList<>();
        SQLiteDatabase baseDdatos = baseDeDatos.getReadableDatabase();

        String sql = "SELECT cal.id, cal.descripcion, cal.nota, cal.fecha, cal.materia_cal, " +
                "cal.alumno_id, cal.trimestre_id, cal.entrega_id, profe.nombre_prof, profe.apellido_prof " +
                "FROM calificaciones cal " +
                "LEFT JOIN profesores profe ON profe.id = cal.profesor_id " +
                "WHERE cal.alumno_id = ? " +
                "ORDER BY cal.fecha DESC";

        Cursor fila = baseDdatos.rawQuery(sql, new String[]{ String.valueOf(alumId) });

        if (fila.moveToFirst()) {
            do {
                Calificacion cal = new Calificacion();
                cal.id = fila.getInt(0);
                cal.descripcion = fila.getString(1);
                cal.nota = fila.getDouble(2);
                cal.fecha = fila.getString(3);
                cal.materia_cal = fila.getString(4);
                cal.alumno_id = fila.getInt(5);
                cal.trimestre_id = fila.getInt(6);
                cal.entrega_id = fila.getInt(7);
                cal.nombre_profe = fila.getString(8);
                cal.apellido_profe = fila.getString(9);
                lista.add(cal);
            } while (fila.moveToNext());
        }
        fila.close();
        baseDdatos.close();
        return lista;
    }
}
