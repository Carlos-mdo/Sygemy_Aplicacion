package Servicios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import Datos.AdminSQLiteOpenHelper;

public class ServicioAdmin {
    protected Context context;
    protected boolean estado;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase base_Datos;

    public ServicioAdmin(Context context){
        this.context = context;
        admin = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
        base_Datos = admin.getWritableDatabase();
    }

    public boolean buscarAdmin(String etUsuario, String etContrasenia) {

        Cursor usuarAdmin = base_Datos.rawQuery("select usuario from usuarios", null);
        Cursor contraAdmin = base_Datos.rawQuery("select contrasenia from usuarios",null);

        if (usuarAdmin.moveToFirst()) {
            if(contraAdmin.moveToFirst()){

                if (etUsuario.equals(usuarAdmin.getString(0)) && etContrasenia.equals(contraAdmin.getString(0))) {
                    estado = true;
                }
            }
        } else {
            Toast.makeText(context, "no existen datos de usuario en la tabla", Toast.LENGTH_SHORT).show();
            estado = false;
        }

        usuarAdmin.close();
        contraAdmin.close();

        return estado;
    }
}
