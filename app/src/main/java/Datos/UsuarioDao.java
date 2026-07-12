package Datos;
import android.content.Context;
import android.content.SharedPreferences;
public class UsuarioDao {
    private static final String sesion = "sesion_usuario";
    private static final String profe_id = "profesor_id";
    private static final String materia_profe = "materia_profe";
    private static final String KEY_ALUM_ID = "alumnoId";

    public static void guardarProfesor(Context context, int profesorId, String materia) {
        SharedPreferences guardado = context.getSharedPreferences(sesion, Context.MODE_PRIVATE);
        guardado.edit().putInt(profe_id, profesorId).putString(materia_profe, materia).apply();
    }
    public static int obtenerProfeId(Context context) {
        return context.getSharedPreferences(sesion, Context.MODE_PRIVATE).getInt(profe_id, -1);
    }
    public static String obtenerMateria(Context context) {
        return context.getSharedPreferences(sesion, Context.MODE_PRIVATE).getString(materia_profe, "");
    }
    public static void guardarAlumno(Context context, int alumnoId) {
        SharedPreferences prefs = context.getSharedPreferences(sesion, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_ALUM_ID, alumnoId).apply();
    }

    public static int obtenerAlumnoId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(sesion, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_ALUM_ID, -1);
    }
    public static void cerrarSesion(Context context) {
        context.getSharedPreferences(sesion, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
