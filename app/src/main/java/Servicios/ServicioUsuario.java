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
    protected boolean estado;
    private AdminSQLiteOpenHelper usuario;
    private SQLiteDatabase base_Datos;
    private ContentValues usuarioRegis = new ContentValues();

    public ServicioUsuario (Context context){
        this.context = context;
        usuario = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
        base_Datos = usuario.getWritableDatabase();
    }

    public String BuscarUsuario(EditText etUsuario, EditText etContrasenia) {

        String usuarioTexto = etUsuario.getText().toString();
        String contraseniaTexto = etContrasenia.getText().toString();
        String rol = null;

        Cursor cursor = base_Datos.rawQuery(
                "SELECT * FROM usuarios WHERE usuario=? AND contrasenia=?",
                new String[]{usuarioTexto, contraseniaTexto}
        );

        if (cursor.moveToFirst()) {
            rol = cursor.getString(3);
        }

        cursor.close();

        return rol;
    }
}
