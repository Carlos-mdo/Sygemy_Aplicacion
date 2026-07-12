package Datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entidades.Actividad;

public class ActividadDao {
    private final AdminSQLiteOpenHelper baseDeDatos;

    public ActividadDao(Context context) {
        baseDeDatos = new AdminSQLiteOpenHelper(context,"BD_Sygemy",null,1);
    }

    public boolean insertar(Actividad actividad) {
        SQLiteDatabase bd = baseDeDatos.getWritableDatabase();
        long resultado = bd.insert("actividad", null, actividad.Valores());
        bd.close();
        return resultado != -1;
    }

    public List<Actividad> obtenerTodas() {
        List<Actividad> listAct = new ArrayList<>();
        SQLiteDatabase bd = baseDeDatos.getReadableDatabase();

        Cursor fila = bd.query("actividad", null, null, null, null, null, "id_act DESC");

        if (fila.moveToFirst()) {
            do {
                listAct.add(new Actividad(
                    fila.getInt(fila.getColumnIndexOrThrow("id_act")),
                    fila.getString(fila.getColumnIndexOrThrow("tipo_act")),
                    fila.getString(fila.getColumnIndexOrThrow("titulo_act")),
                    fila.getString(fila.getColumnIndexOrThrow("descripcion_act")),
                    fila.getString(fila.getColumnIndexOrThrow("fecha_act")),
                    fila.getString(fila.getColumnIndexOrThrow("url_act")),
                    fila.getString(fila.getColumnIndexOrThrow("archivo_nombre")),
                    fila.getString(fila.getColumnIndexOrThrow("archivo_url")),
                    fila.getString(fila.getColumnIndexOrThrow("materia_act")),
                    fila.getInt(fila.getColumnIndexOrThrow("trimestre_id"))
                ));
            } while (fila.moveToNext());
        }
        fila.close();
        bd.close();
        return listAct;
    }
}
