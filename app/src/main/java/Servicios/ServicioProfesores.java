package Servicios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import Datos.AdminSQLiteOpenHelper;

public class ServicioProfesores {
//    protected Context context;
//    protected boolean estado;
//    private AdminSQLiteOpenHelper profesores;
//    private SQLiteDatabase base_Datos;
//    private ContentValues profeRegis = new ContentValues();
//
//    public ServicioProfesores (Context context){
//        this.context = context;
//        profesores = new AdminSQLiteOpenHelper(context, "BD_Sygemy", null, 1);
//        base_Datos = profesores.getWritableDatabase();
//    }
//
//    public boolean buscarProfe(EditText etUsuario, EditText etContrasenia) {
//
//        profeRegis.put("usuario","profe");
//        profeRegis.put("contrasenia","profe123");
//        profeRegis.put("rol","profe");
//
//        base_Datos.insert("usuarios",null,profeRegis);
//
//        Cursor usuarProfe = base_Datos.rawQuery("select usuario from usuarios", null);
//        Cursor contraProfe = base_Datos.rawQuery("select contrasenia from usuarios",null);
//
//        if (usuarProfe.moveToFirst()) {
//            if(contraProfe.moveToFirst()){
//
//                if (etUsuario.getText().toString().equals(usuarProfe.getString(0)) && etContrasenia.getText().toString().equals(contraProfe.getString(0))) {
//                    estado = true;
//                }
//            }
//        } else {
//            Toast.makeText(context, "no existen datos de usuario en la tabla", Toast.LENGTH_SHORT).show();
//            estado = false;
//        }
//
//        usuarProfe.close();
//        contraProfe.close();
//
//        return estado;
//    }
}
