package com.aplicacion.gestion_escolar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapter.DashEventAdapter;
import Adapter.EntAdapter;
import Datos.AdminSQLiteOpenHelper;
import Datos.EntregaDao;
import Datos.UsuarioDao;
import Entidades.EntregaPendiente;
import Entidades.EventDashboard;
import Ventana.Alumno.Fragment_Cursos;
import Ventana.Alumno.Fragment_Horarios;
import Ventana.Alumno.Fragment_Material;
import Ventana.Alumno.Fragment_Notas;
import Ventana.Profesor.Fragment_Actividad;
import Ventana.Profesor.Fragment_Calificaciones;
import Ventana.Profesor.Fragment_Correciones;
import Ventana.Profesor.Fragment_Cursos_Profesores;

public class MenuUsuarioActivity extends AppCompatActivity implements ControllerDrawerMenu{

    private ImageButton btnDesplegar;
    private DrawerLayout drawLayout;
    private NavigationView naView;
    private int id;
    private View contenedorFragment, scrollBienvenida;
    private TextView txtUsuario;
    private ImageView imgUsuario;
    private RecyclerView rvTareasPendientes;
    private View llDashboardAlumno;
    private TextView tvSinTareas;
    private AdminSQLiteOpenHelper helper;
    private SQLiteDatabase baseDeDatos;
    private String rolActual;
    private  int alumnoIdActual = -1;
    private  int profesorIdActual = -1;
    private View llDashboardProfesor;
    private RecyclerView rvEntregasPendientesProfesor;
    private TextView tvSinPendientesProfesor;
    private final ActivityResultLauncher<String[]> selectorImagen = registerForActivityResult(new ActivityResultContracts.OpenDocument(), this::onImagenSeleccionada);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        naView = findViewById(R.id.navigationView);
        drawLayout = findViewById(R.id.layoutEscuela);
        btnDesplegar = findViewById(R.id.iBtnMenu);
        contenedorFragment = findViewById(R.id.contenedorFragment);
        scrollBienvenida = findViewById(R.id.scrollBienvenida);

        txtUsuario = findViewById(R.id.txtUsuario);
        imgUsuario = findViewById(R.id.imageView);
        llDashboardAlumno = findViewById(R.id.llDashboardAlumno);
        rvTareasPendientes = findViewById(R.id.rvTareasPendientes);
        tvSinTareas = findViewById(R.id.tvSinTareas);

        llDashboardProfesor = findViewById(R.id.llDashboardProfesor);
        rvEntregasPendientesProfesor = findViewById(R.id.rvEntregasPendientesProfesor);
        tvSinPendientesProfesor = findViewById(R.id.tvSinPendientesProfesor);
        rvEntregasPendientesProfesor.setLayoutManager(new LinearLayoutManager(this));

        rvTareasPendientes.setLayoutManager(new LinearLayoutManager(this));

        btnDesplegar.setOnClickListener(view -> desplegarMenu());

        seleccionMenu();
        roles();
        cargarDatosDeSesion();
    }
    private void cargarDatosDeSesion() {
        SharedPreferences spref = getSharedPreferences("Roles", Context.MODE_PRIVATE);
        rolActual = spref.getString("rol", "");

        helper = new AdminSQLiteOpenHelper(this, "BD_Sygemy", null, 1);
        baseDeDatos = helper.getReadableDatabase();

        if ("alumn".equals(rolActual)) {
            cargarDatosAlumno();
        } else if ("profe".equals(rolActual)) {
            cargarDatosProfesor();
        } else {
            txtUsuario.setText("Administrador");
        }
    }
    private void cargarDatosAlumno() {
        alumnoIdActual = UsuarioDao.obtenerAlumnoId(this);
        if (alumnoIdActual == -1) {
            txtUsuario.setText("Alumno/a");
            return;
        }

        Cursor fila_carga_alum = baseDeDatos.rawQuery(
                "SELECT nombre_alum, apellido_alum, curso_alum, foto_alum FROM alumnos WHERE id = ?",
                new String[]{String.valueOf(alumnoIdActual)});

        String curso = null;
        if (fila_carga_alum.moveToFirst()) {
            String nombre = fila_carga_alum.getString(0);
            String apellido = fila_carga_alum.getString(1);
            curso = fila_carga_alum.getString(2);
            String fotoUri = fila_carga_alum.getString(3);

            txtUsuario.setText(nombre + " " + apellido);
            mostrarFoto(fotoUri);
        }
        fila_carga_alum.close();

        Log.d("DashboardAlumno", "curso_alum leído de la BD = [" + curso + "]");

        imgUsuario.setOnClickListener(v -> selectorImagen.launch(new String[]{"image/*"}));

        llDashboardAlumno.setVisibility(View.VISIBLE);
        if (curso != null) {
            cargarDashboardAlumno(curso, alumnoIdActual);
        }else {
            Log.d("DashboardAlumno", "curso es null -> nunca se llega a buscar actividades");
            mostrarListaVacia(rvTareasPendientes, tvSinTareas, new ArrayList<>());
        }
    }
    private void mostrarFoto(String fotoUri) {
        if (fotoUri == null || fotoUri.trim().isEmpty()) {
            imgUsuario.setImageResource(R.drawable.ic_default_avatar);
            return;
        }
        try {
            imgUsuario.setImageURI(Uri.parse(fotoUri));
        } catch (RuntimeException e) {
            imgUsuario.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    private void onImagenSeleccionada(Uri uriSeleccionada) {
        if (uriSeleccionada == null) { return;}

        getContentResolver().takePersistableUriPermission(uriSeleccionada, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ContentValues valores = new ContentValues();
        if ("alumn".equals(rolActual) && alumnoIdActual != -1) {
            valores.put("foto_alum", uriSeleccionada.toString());
            baseDeDatos.update("alumnos", valores, "id = ?", new String[]{String.valueOf(alumnoIdActual)});
        } else if ("profe".equals(rolActual) && profesorIdActual != -1) {
            valores.put("foto_prof", uriSeleccionada.toString());
            baseDeDatos.update("profesores", valores, "id = ?", new String[]{String.valueOf(profesorIdActual)});
        } else {
            return;
        }

        mostrarFoto(uriSeleccionada.toString());
        Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
    }
    private void cargarDashboardAlumno(String curso, int alumnoId) {
        List<String> materias = obtenerMateriasDelCurso(curso);
        if (materias.isEmpty()) {
            Log.d("DashboardAlumno", "No hay materias en 'horarios' para curso=[" + curso + "] -> se corta acá, dashboard queda vacío");
            mostrarListaVacia(rvTareasPendientes, tvSinTareas, new ArrayList<>());
            return;
        }

        String placeholders = construirPlaceholders(materias.size());

        List<EventDashboard> tareasPendientes = new ArrayList<>();
        String sql = "SELECT a.id_act, a.titulo_act, a.materia_act, a.fecha_act, " +
                "p.nombre_prof, p.apellido_prof " +
                "FROM actividad a " +
                "LEFT JOIN profesores p ON p.id = a.profesor_id " +
                "WHERE TRIM(a.materia_act) COLLATE NOCASE IN (" + placeholders + ") " +
                "AND NOT EXISTS (SELECT 1 FROM entregas e WHERE e.actividad_id = a.id_act AND e.alumno_id = ?) " +
                "ORDER BY a.fecha_act ASC LIMIT 5";

        List<String> args = new ArrayList<>(materias);
        args.add(String.valueOf(alumnoId));
        Log.d("DashboradAlumno", "SQL dashboard = " + sql);
        Log.d("DashboradAlumno", "args = " + args);

        Cursor cursor = baseDeDatos.rawQuery(sql, args.toArray(new String[0]));
        Log.d("DashboradAlumno", "filas devueltas por la query = " + cursor.getCount());

        while (cursor.moveToNext()) {
            String nombreProfe = cursor.getString(4);
            String apellidoProfe = cursor.getString(5);
            String profesorTexto = (nombreProfe != null) ? nombreProfe + " " + apellidoProfe : "Profesor no asignado";

            tareasPendientes.add(new EventDashboard(
                    cursor.getString(1),
                    cursor.getString(2),
                    formatearFecha(cursor.getString(3)),
                    EventDashboard.TIPO_ENTREGA,
                    profesorTexto));
        }
        cursor.close();
        Log.d("DashboardAlumno", "tareasPendientes final = " + tareasPendientes.size());
        mostrarListaVacia(rvTareasPendientes, tvSinTareas, tareasPendientes);
    }

    private List<String> obtenerMateriasDelCurso(String curso) {
        List<String> materias = new ArrayList<>();
        Cursor cursor = baseDeDatos.rawQuery(
                "SELECT DISTINCT materia_hor FROM horarios " +
                        "WHERE TRIM(curso_hor) = TRIM(?) COLLATE NOCASE",
                new String[]{curso});
        while (cursor.moveToNext()) {
            materias.add(cursor.getString(0));
        }
        cursor.close();
        Log.d("DashboardAlumno", "obtenerMateriasDelCurso([" + curso + "]) -> " + materias);
        return materias;
    }
    private String construirPlaceholders(int cantidad) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cantidad; i++) {
            sb.append(i == 0 ? "?" : ",?");
        }
        return sb.toString();
    }
    private String formatearFecha(String fechaIso) {
        if (fechaIso == null) return "";
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat salida = new SimpleDateFormat("dd/MM", Locale.getDefault());
            return salida.format(entrada.parse(fechaIso));
        } catch (Exception e) {
            return fechaIso;
        }
    }
    private void mostrarListaVacia(RecyclerView recyclerView, TextView tvVacio, List<EventDashboard> eventos) {
        DashEventAdapter adapter = new DashEventAdapter();
        adapter.setEventos(eventos);
        recyclerView.setAdapter(adapter);

        boolean vacio = eventos.isEmpty();
        recyclerView.setVisibility(vacio ? View.GONE : View.VISIBLE);
        tvVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
    }
    private void cargarDatosProfesor() {
        profesorIdActual = UsuarioDao.obtenerProfeId(this);
        if (profesorIdActual == -1) {
            txtUsuario.setText("Profesor/a");
            return;
        }
        Cursor fila_carga_profe = baseDeDatos.rawQuery(
                "SELECT nombre_prof, apellido_prof, foto_prof FROM profesores WHERE id = ?",
                new String[]{String.valueOf(profesorIdActual)});

        if (fila_carga_profe.moveToFirst()) {
            String nombre = fila_carga_profe.getString(0);
            String apellido = fila_carga_profe.getString(1);
            String fotoUri = fila_carga_profe.getString(2);

            txtUsuario.setText(nombre + " " + apellido);
            mostrarFoto(fotoUri);
        } else {
            txtUsuario.setText("Profesor/a");
        }
        fila_carga_profe.close();

        imgUsuario.setOnClickListener(v -> selectorImagen.launch(new String[]{"image/*"}));
        llDashboardProfesor.setVisibility(View.VISIBLE);
        cargarDashboardProfesor();
    }
    private void cargarDashboardProfesor() {
       //String materia = UsuarioDao.obtenerMateria(this);
        EntregaDao entregaDao = new EntregaDao(this);
        List<EntregaPendiente> pendientes = entregaDao.obtenerPendientes(profesorIdActual);

        boolean vacio = pendientes.isEmpty();
        rvEntregasPendientesProfesor.setVisibility(vacio ? View.GONE : View.VISIBLE);
        tvSinPendientesProfesor.setVisibility(vacio ? View.VISIBLE : View.GONE);
        llDashboardProfesor.setVisibility(View.VISIBLE);
        EntAdapter adapter = new EntAdapter(pendientes, this, entrega -> {
        });
        rvEntregasPendientesProfesor.setAdapter(adapter);
    }
    private void cargarFragment(Fragment fragment) {
        contenedorFragment.setVisibility(View.VISIBLE);
        scrollBienvenida.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, fragment).commit();

        drawLayout.closeDrawer(GravityCompat.START);
    }
    public void desplegarMenu() {
        if (drawLayout.isDrawerOpen(GravityCompat.START)) {
            cerrarNav();
        } else {
            abrirNav();
        }
    }
    public void seleccionMenu(){
        SharedPreferences spref = getSharedPreferences("Roles", Context.MODE_PRIVATE);
        String rol = spref.getString("rol","");

        naView.setNavigationItemSelectedListener(item -> {

            id = item.getItemId();

            if(id == R.id.nav_inicio){

                Intent intent = new Intent(getBaseContext(), MenuUsuarioActivity.class);
                startActivity(intent);

                finish();
                return true;
            }
            if(rol.equals("profe")){

                if(id == R.id.nav_actividad){
                    cargarFragment(new Fragment_Actividad());
                    return true;
                }
                if(id == R.id.nav_calificaciones){
                    cargarFragment(new Fragment_Calificaciones());
                    return true;
                }
                if(id == R.id.nav_correciones){
                    cargarFragment(new Fragment_Correciones());
                    return true;
                }
            }
            if(rol.equals("alumn")){

                if(id == R.id.nav_material){
                    cargarFragment(new Fragment_Material());
                    return true;
                }
                if(id == R.id.nav_horario){
                    cargarFragment(new Fragment_Horarios());
                    return true;
                }
                if(id == R.id.nav_notas){
                    cargarFragment(new Fragment_Notas());
                    return true;
                }
                if(id == R.id.nav_cursos_alum){
                    cargarFragment(new Fragment_Cursos());
                    return true;
                }
            }
            if(id == R.id.nav_cerrar){
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

                finish();
                return true;
            }
            return false;
        });
    }

    public void roles(){
        SharedPreferences spref = getSharedPreferences("Roles", Context.MODE_PRIVATE);
        String rol = spref.getString("rol","sin_rol");

        activarRol(rol);
    }

    public void activarRol(String rol){
        Menu menu = naView.getMenu();

        menu.setGroupVisible(R.id.grupo_profesores, rol.equals("profe"));
        menu.setGroupVisible(R.id.grupo_alumnos, rol.equals("alumn"));
        menu.setGroupVisible(R.id.grupo_admin, rol.equals("admin"));

    }

    @Override
    public void abrirNav() {
        drawLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public void cerrarNav() {
        drawLayout.closeDrawer(GravityCompat.START);
    }
}