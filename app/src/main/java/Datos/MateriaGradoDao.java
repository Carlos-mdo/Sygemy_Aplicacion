package Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MateriaGradoDao {
    private AdminSQLiteOpenHelper dbHelper;

    public MateriaGradoDao(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
    }

    public void asignarMateria(String curso, String materia) {
        ContentValues cv = new ContentValues();
        cv.put("curso", curso);
        cv.put("materia", materia);
        dbHelper.getWritableDatabase().insertWithOnConflict(
                "materias_grado", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void quitarMateria(String curso, String materia) {
        dbHelper.getWritableDatabase().delete(
                "materias_grado", "curso = ? AND materia = ?",
                new String[]{curso, materia});
    }

    public List<String> obtenerMateriasDeCurso(String curso) {
        List<String> lista = new ArrayList<>();
        Cursor c = dbHelper.getReadableDatabase().rawQuery(
                "SELECT materia FROM materias_grado WHERE curso = ? ORDER BY materia",
                new String[]{curso});
        while (c.moveToNext()) lista.add(c.getString(0));
        c.close();
        return lista;
    }

    public void cerrar() {
        dbHelper.close();
    }

}
