package Ventana.Profesor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.FragmentBase;
import com.aplicacion.gestion_escolar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapter.EntAdapter;
import Datos.AdminSQLiteOpenHelper;
import Datos.EntregaDao;
import Datos.UsuarioDao;
import Entidades.EntregaPendiente;

public class Fragment_Correciones extends FragmentBase {
    private RecyclerView rvCorrecciones;
    private TextView txtSinPendientes;
    private List<EntregaPendiente> listaPendientes = new ArrayList<>();
    private final List<Integer> listaTrimestres = new ArrayList<>();
    private final List<String> nombresTrimestres = new ArrayList<>();
    private SQLiteDatabase bd_correccion;
    private String materia_Profe;
    private boolean mostrarCorregidas = false;
    private Button btnPendientes , btnCorregidas;
    private int profesorIdActual = -1;

    public Fragment_Correciones() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__correciones, container, false);
        rvCorrecciones = view.findViewById(R.id.rvCorrecciones);
        txtSinPendientes = view.findViewById(R.id.tvSinPendientes);
        btnPendientes   = view.findViewById(R.id.btnPendientes);
        btnCorregidas   = view.findViewById(R.id.btnCorregidas);
        rvCorrecciones.setLayoutManager(new LinearLayoutManager(requireContext()));


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "BD_Sygemy", null, 1);
        bd_correccion = admin.getWritableDatabase();
        materia_Profe = UsuarioDao.obtenerMateria(requireContext());
        profesorIdActual = UsuarioDao.obtenerProfeId(requireContext());

        btnPendientes.setOnClickListener(v -> {
            mostrarCorregidas = false;
            cargarLista();
        });
        btnCorregidas.setOnClickListener(v -> {
            mostrarCorregidas = true;
            cargarLista();
        });

        cargarTrimestres();
        cargarPendientes();
        return view;
    }
    private void cargarTrimestres() {
        listaTrimestres.clear();
        nombresTrimestres.clear();

        Cursor fila = bd_correccion.rawQuery("SELECT id, nombre FROM trimestres ORDER BY id", null);
        if (fila.moveToFirst()) {
            do {
                listaTrimestres.add(fila.getInt(0));
                nombresTrimestres.add(fila.getString(1));
            } while (fila.moveToNext());
        }
        fila.close();
    }
    private void cargarLista() {
        EntregaDao daoEnt = new EntregaDao(requireContext());
        listaPendientes = mostrarCorregidas ? daoEnt.obtenerCorregidas(profesorIdActual) : daoEnt.obtenerPendientes(profesorIdActual);

        if (listaPendientes.isEmpty()) {
            txtSinPendientes.setText(mostrarCorregidas ? "Todavía no hay entregas corregidas" : "No hay entregas pendientes de corregir");
            txtSinPendientes.setVisibility(View.VISIBLE);
            rvCorrecciones.setVisibility(View.GONE);
        } else {
            txtSinPendientes.setVisibility(View.GONE);
            rvCorrecciones.setVisibility(View.VISIBLE);
        }
        EntAdapter adapter = new EntAdapter(listaPendientes, requireContext(), this::onEntregaClick);
        rvCorrecciones.setAdapter(adapter);
    }
    private void onEntregaClick(EntregaPendiente entrega) {
        if (entrega.corregida) {
            mostrarDetalle_Corregida(entrega);
        } else {
            mostrarDetallePendiente(entrega);
        }
    }
    private void mostrarDetalle_Corregida(EntregaPendiente entrega) {

        LinearLayout layout = new LinearLayout(requireContext());
        TextView txtAlumno_corregida = new TextView(requireContext());
        TextView txtActividad_corregida = new TextView(requireContext());
        TextView txtNota_corregida = new TextView(requireContext());
        TextView txtComentario_corregida = new TextView(requireContext());
        TextView txtAdjunto_corregida = new TextView(requireContext());
        boolean tieneUrlCorregida = entrega.url_Ent != null && !entrega.url_Ent.isEmpty();
        boolean tieneArchivoCorregida = entrega.arch_Url != null && !entrega.arch_Url.isEmpty();

        if (tieneUrlCorregida) {
            txtAdjunto_corregida.setText("Enlace: " + entrega.url_Ent);
            txtAdjunto_corregida.setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entrega.url_Ent))));
        } else if (tieneArchivoCorregida) {
            txtAdjunto_corregida.setText("Archivo: " + entrega.arch_Nombre);
            txtAdjunto_corregida.setOnClickListener(v -> abrirArchivoEntrega(entrega));
        } else {
            txtAdjunto_corregida.setText("Sin adjunto");
        }

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);
        txtAlumno_corregida.setText("Alumno: " + entrega.nombre_Alum);
        txtActividad_corregida.setText("Actividad: " + entrega.titulo_Actividad);
        txtNota_corregida.setText("Nota: " + entrega.nota);
        txtComentario_corregida.setText("Comentario del alumno: " + (entrega.comentario != null && !entrega.comentario.isEmpty() ? entrega.comentario : "Sin comentario"));

        layout.addView(txtAlumno_corregida);
        layout.addView(txtActividad_corregida);
        layout.addView(txtNota_corregida);
        layout.addView(txtComentario_corregida);
        layout.addView(txtAdjunto_corregida);

        new AlertDialog.Builder(requireContext()).setTitle("Entrega corregida").setView(layout).setPositiveButton("Cerrar", null).show();
    }
    private void cargarPendientes() {
        EntregaDao daoEnt = new EntregaDao(requireContext());
        listaPendientes = daoEnt.obtenerPendientes(profesorIdActual);

        if (listaPendientes.isEmpty()) {
            txtSinPendientes.setVisibility(View.VISIBLE);
            rvCorrecciones.setVisibility(View.GONE);
        } else {
            txtSinPendientes.setVisibility(View.GONE);
            rvCorrecciones.setVisibility(View.VISIBLE);
        }
        EntAdapter adapter = new EntAdapter(listaPendientes, requireContext(), this::onEntregaClick);
        rvCorrecciones.setAdapter(adapter);
    }

    private void abrirArchivoEntrega(EntregaPendiente entrega) {
        try {
            Uri uri = Uri.parse(entrega.arch_Url);
            String mime = obtenerMimeType(entrega.arch_Nombre);

            if (mime.equals("*/*")) {
                String tipoResolver = requireContext().getContentResolver().getType(uri);
                if (tipoResolver != null) mime = tipoResolver;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "No hay una app instalada para abrir este tipo de archivo", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "No se puede abrir el archivo", Toast.LENGTH_SHORT).show();
        }
    }
    private String obtenerMimeType(String nombreArchivo) {
        String mime = "*/*";
        if (nombreArchivo != null) {
            int punto = nombreArchivo.lastIndexOf('.');
            if (punto >= 0 && punto < nombreArchivo.length() - 1) {
                String extension = nombreArchivo.substring(punto + 1).toLowerCase();
                String tipo = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                if (tipo != null) mime = tipo;
            }
        }
        return mime;
    }
    private void mostrarDetallePendiente(EntregaPendiente entrega) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        TextView txtAlumno = new TextView(requireContext());
        txtAlumno.setText("Alumno: " + entrega.nombre_Alum);

        TextView txtActividad = new TextView(requireContext());
        txtActividad.setText("Actividad: " + entrega.titulo_Actividad);

        TextView txtFecha = new TextView(requireContext());
        txtFecha.setText("Fecha de entrega: " + entrega.fecha_Ent);

        TextView txtComentario = new TextView(requireContext());
        String coment = entrega.comentario;
        if (coment == null || coment.isEmpty()) coment = "Sin comentario";
        txtComentario.setText("Comentario: " + coment);

        TextView txtAdjunto = new TextView(requireContext());
        boolean tieneUrl = entrega.url_Ent != null && !entrega.url_Ent.isEmpty();
        boolean tieneArchivo = entrega.arch_Url != null && !entrega.arch_Url.isEmpty();

        if (tieneUrl) {
            txtAdjunto.setText("Enlace: " + entrega.url_Ent);
            txtAdjunto.setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entrega.url_Ent))));
        } else if (tieneArchivo) {
            txtAdjunto.setText("Archivo: " + entrega.arch_Nombre);
            txtAdjunto.setOnClickListener(v -> abrirArchivoEntrega(entrega));
        } else {
            txtAdjunto.setText("Sin adjunto");
        }

        layout.addView(txtAlumno);
        layout.addView(txtActividad);
        layout.addView(txtFecha);
        layout.addView(txtComentario);
        layout.addView(txtAdjunto);

        new AlertDialog.Builder(requireContext())
                .setTitle("Entrega pendiente")
                .setView(layout)
                .setPositiveButton("Cerrar", null)
                .show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bd_correccion != null && bd_correccion.isOpen()) {
            bd_correccion.close();
        }
    }
}