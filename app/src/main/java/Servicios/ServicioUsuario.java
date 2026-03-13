package Servicios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import Datos.AdminSQLiteOpenHelper;

public class ServicioUsuario {

    protected Context context;
    private AdminSQLiteOpenHelper usuario;
    private SQLiteDatabase base_Datos;
    public String rol;

    public ServicioUsuario (Context context){
        this.context = context;
        usuario = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
        base_Datos = usuario.getWritableDatabase();
    }

    public String buscarUsuario(String etUsuario, String etContrasenia) {


        Cursor cursor = base_Datos.rawQuery(
                "SELECT * FROM usuarios WHERE usuario=? AND contrasenia=?",
                new String[]{etUsuario, etContrasenia}
        );

        if (cursor.moveToFirst()) {
            rol = cursor.getString(3);
        }else{
            rol = null;
        }

        cursor.close();

        return rol;
    }


}
