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
        listaPendientes = mostrarCorregidas ? daoEnt.obtenerCorregidas(materia_Profe) : daoEnt.obtenerPendientes(materia_Profe);

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
            abrirCorreccion(entrega);
        }
    }
    private void mostrarDetalle_Corregida(EntregaPendiente entrega) {

        LinearLayout layout = new LinearLayout(requireContext());
        TextView txtAlumno_corregida = new TextView(requireContext());
        TextView txtActividad_corregida = new TextView(requireContext());
        TextView txtNota_corregida = new TextView(requireContext());
        TextView txtComentario_corregida = new TextView(requireContext());

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

        new AlertDialog.Builder(requireContext()).setTitle("Entrega corregida").setView(layout).setPositiveButton("Cerrar", null).show();
    }
    private void cargarPendientes() {
        EntregaDao daoEnt = new EntregaDao(requireContext());
        listaPendientes = daoEnt.obtenerPendientes(materia_Profe);

        if (listaPendientes.isEmpty()) {
            txtSinPendientes.setVisibility(View.VISIBLE);
            rvCorrecciones.setVisibility(View.GONE);
        } else {
            txtSinPendientes.setVisibility(View.GONE);
            rvCorrecciones.setVisibility(View.VISIBLE);
        }
        EntAdapter adapter = new EntAdapter(listaPendientes, requireContext(), this::abrirCorreccion);
        rvCorrecciones.setAdapter(adapter);
    }

    private void abrirCorreccion(EntregaPendiente entrega) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        TextView txtAlumno_correcion = new TextView(requireContext());
        TextView txtActividad_correcion = new TextView(requireContext());
        TextView txtComentario_correcion = new TextView(requireContext());
        TextView txtAdjunto_correcion = new TextView(requireContext());
        Spinner spinnerTrimestre = new Spinner(requireContext());
        EditText etNota = new EditText(requireContext());

        txtAlumno_correcion.setText("Alumno: " + entrega.nombre_Alum);
        txtActividad_correcion.setText("Actividad: " + entrega.titulo_Actividad);

        String coment = entrega.comentario;
        if (coment == null || coment.isEmpty()) {
            coment = "Sin comentario";
        }
        txtComentario_correcion.setText("Comentario: " + coment);

        boolean tieneUrl = entrega.url_Ent != null && !entrega.url_Ent.equals("");
        boolean tieneArchivo = entrega.arch_Url != null && entrega.arch_Url.length() > 0;

        if (tieneUrl == true) {
            txtAdjunto_correcion.setText("Enlace: " + entrega.url_Ent);
            txtAdjunto_correcion.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entrega.url_Ent));
                startActivity(intent);
            });
        } else if (tieneArchivo) {
            txtAdjunto_correcion.setText("Archivo: " + entrega.arch_Nombre);
            txtAdjunto_correcion.setOnClickListener(v -> {
                try {
                    Uri url = Uri.parse(entrega.arch_Url);
                    String tipoArchivo = requireContext().getContentResolver().getType(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (tipoArchivo != null) {
                        intent.setDataAndType(url, tipoArchivo);
                    } else {
                        intent.setDataAndType(url, "*/*");
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "No se puede abrir el archivo", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            txtAdjunto_correcion.setText("Sin adjunto");
        }

        ArrayAdapter<String> trimesAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, nombresTrimestres);
        trimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrimestre.setAdapter(trimesAdapter);

        etNota.setHint("Nota (ej: 8.5)");
        etNota.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        layout.addView(txtAlumno_correcion);
        layout.addView(txtActividad_correcion);
        layout.addView(txtComentario_correcion);
        layout.addView(txtAdjunto_correcion);
        layout.addView(spinnerTrimestre);
        layout.addView(etNota);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Corregir entrega");
            builder.setView(layout);
            builder.setPositiveButton("Guardar nota", (dialogo, which) -> {

            String valorNota = etNota.getText().toString().trim();
            if (valorNota.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese una nota", Toast.LENGTH_SHORT).show();
                return;
            }

            double nota = 0;
            try {
                nota = Double.parseDouble(valorNota);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Nota inválida", Toast.LENGTH_SHORT).show();
                return;
            }
            int posTrimestre = spinnerTrimestre.getSelectedItemPosition();
            if (posTrimestre < 0 || posTrimestre >= listaTrimestres.size()) {
                Toast.makeText(requireContext(), "Seleccione un trimestre", Toast.LENGTH_SHORT).show();
                return;
            }
            int trimestreId = listaTrimestres.get(posTrimestre);
            String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            ContentValues valores = new ContentValues();
            valores.put("alumno_id", entrega.alumno_id);
            valores.put("trimestre_id", trimestreId);
            valores.put("descripcion", entrega.titulo_Actividad);
            valores.put("nota", nota);
            valores.put("fecha", fecha);
            valores.put("materia_cal", materia_Profe);
            valores.put("entrega_id", entrega.entrega_id);

            long resultado = bd_correccion.insert("calificaciones", null, valores);
            if (resultado != -1) {
                Toast.makeText(requireContext(), "Entrega corregida", Toast.LENGTH_SHORT).show();
                cargarPendientes();
            } else {
                Toast.makeText(requireContext(), "Error al guardar la nota", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bd_correccion != null && bd_correccion.isOpen()) {
            bd_correccion.close();
        }
    }
}