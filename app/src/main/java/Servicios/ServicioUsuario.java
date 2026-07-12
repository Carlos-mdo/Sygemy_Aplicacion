package Servicios;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Datos.AdminSQLiteOpenHelper;
import Entidades.Usuarios;


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

    public Usuarios buscarUsuario(String etUsuario, String etContrasenia) {

        Cursor cursor = base_Datos.rawQuery(
                "SELECT * FROM usuarios WHERE usuario=? AND contrasenia=?",
                new String[]{etUsuario, etContrasenia}
        );

        Usuarios usuarioEncontrado = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String usuario = cursor.getString(1);
            String contrasenia = cursor.getString(2);
            String rolEncontrado = cursor.getString(3);

            usuarioEncontrado = new Usuarios(id, usuario, contrasenia, rolEncontrado);
            rol = rolEncontrado;
        } else {
            rol = null;
        }

        cursor.close();
        return usuarioEncontrado;
    }

}
