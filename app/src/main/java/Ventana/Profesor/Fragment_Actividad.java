package Ventana.Profesor;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.FragmentBase;
import com.aplicacion.gestion_escolar.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.ActAdapter;
import Datos.ActividadDao;
import Datos.AdminSQLiteOpenHelper;
import Entidades.Actividad;

public class Fragment_Actividad extends FragmentBase {

    public Fragment_Actividad() {}
    private Button btnDesplegar;
    private Uri archiUrl;
    private String archiNombre;
    private ActivityResultLauncher<String[]> selectArchi;
    private AdminSQLiteOpenHelper admin;
    private LinearLayout layoutArchiSelec;
    private TextView tvNombreArchivo;
    private String materia_profe;
    private RecyclerView rvActividades;
    private ProgressBar progressActividades;
    private ActAdapter adapter;
    private final List<Actividad> listaActividades = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectArchi = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        archiUrl    = uri;
                        archiNombre = obtenNomArchiv(uri);

                        if (tvNombreArchivo != null && layoutArchiSelec != null) {
                            tvNombreArchivo.setText(archiNombre);
                            layoutArchiSelec.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment__actividad, container, false);

        btnDesplegar = view.findViewById(R.id.btnAgregarAct);
        btnDesplegar.setOnClickListener(view1 -> mostrarMenu(view1));
        materia_profe = Datos.UsuarioDao.obtenerMateria(requireContext());
        vincularBotonMenu(view, R.id.iBtnMenu);

        rvActividades = view.findViewById(R.id.rvActividades);
        progressActividades = view.findViewById(R.id.progressActividades);
        rvActividades.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ActAdapter(listaActividades, requireContext(), -1, null);
        rvActividades.setAdapter(adapter);
        cargarActividades();
        return view;
    }
    private void cargarActividades() {
        if (adapter == null) return;

        progressActividades.setVisibility(View.VISIBLE);
        ActividadDao dao = new ActividadDao(requireContext());
        dao.obtenerTodasAsync(resultado -> {
            if (!isAdded()|| adapter == null) return;

            listaActividades.clear();
            listaActividades.addAll(resultado);
            adapter.notifyDataSetChanged();
            progressActividades.setVisibility(View.GONE);
        });
    }
    private void mostrarMenu(View view) {
        PopupMenu popOpciones = new PopupMenu(requireContext(), view);
        popOpciones.getMenu().add(0, 1, 0, "Tarea");
        popOpciones.getMenu().add(0, 2, 0, "Examen");
        popOpciones.getMenu().add(0, 3, 0, "Trabajo Práctico");

        popOpciones.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    mostrarMensaje("Tarea");
                    return true;
                case 2:
                    mostrarMensaje("Examen");
                    return true;
                case 3:
                    mostrarMensaje("Trabajo Práctico");
                    return true;
                default:
                    return false;
            }
        });

        popOpciones.show();
    }

    private void mostrarMensaje(String tipo) {
        archiUrl = null;
        archiNombre = null;
        View ventFlotante = LayoutInflater.from(requireContext()).inflate(R.layout.item_actividad_profe, null);

        EditText etTitulo      = ventFlotante.findViewById(R.id.etTitulo);
        EditText etDescripcion = ventFlotante.findViewById(R.id.etDescripcion);
        EditText etFecha       = ventFlotante.findViewById(R.id.etFecha);
        EditText etUrl         = ventFlotante.findViewById(R.id.etEnlace);
        Button btnSelecArchi   = ventFlotante.findViewById(R.id.btnSelecArchi);

        layoutArchiSelec       = ventFlotante.findViewById(R.id.layoutArchiSelec);
        tvNombreArchivo        = ventFlotante.findViewById(R.id.tvNomArchi);
        ImageButton btnElimina = ventFlotante.findViewById(R.id.btnEliminaArchi);
        Spinner spinnerTrimestre = ventFlotante.findViewById(R.id.spinnerTrimestreAct);

        etFecha.setFocusable(false);
        etFecha.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new android.app.DatePickerDialog(requireContext(), (view1, year, month, day) -> {
                String fechaFormateada = String.format(java.util.Locale.getDefault(),
                        "%02d/%02d/%04d", day, month + 1, year);
                etFecha.setText(fechaFormateada);
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH))
                    .show();
        });

        List<Integer> trimestreIds = new ArrayList<>();
        List<String> trimestreNombres = new ArrayList<>();
        cargarTrimestresAct(trimestreIds, trimestreNombres);

        ArrayAdapter<String> adapterTrim = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, trimestreNombres);
        adapterTrim.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrimestre.setAdapter(adapterTrim);

        btnSelecArchi.setOnClickListener(v -> selectArchi.launch(new String[]{"*/*"}));

        btnElimina.setOnClickListener(v -> {
            archiUrl    = null;
            archiNombre = null;
            layoutArchiSelec.setVisibility(View.GONE);
        });

        new AlertDialog.Builder(requireContext()).setTitle("Agregar " + tipo).setView(ventFlotante)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String titulo      = etTitulo.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    String fecha       = etFecha.getText().toString().trim();
                    String url         = etUrl.getText().toString().trim();
                    String adjunInfo;

                    if (titulo.isEmpty()) {
                        Toast.makeText(requireContext(),"Agregue un titulo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(descripcion.isEmpty()){
                        Toast.makeText(requireContext(),"Agregue la descripcion", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(fecha.isEmpty()){
                        Toast.makeText(requireContext(),"Agregue la fecha", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int posTrimestre = spinnerTrimestre.getSelectedItemPosition();
                    if (posTrimestre < 0 || posTrimestre >= trimestreIds.size()) {
                        Toast.makeText(requireContext(),"Seleccione un trimestre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int trimestreId = trimestreIds.get(posTrimestre);

                    if (!url.isEmpty()) {
                        adjunInfo = "Enlace: " + url;
                    } else if (archiUrl != null) {
                        adjunInfo = "Archivo: " + archiNombre;
                    } else {
                        adjunInfo = "Sin adjunto";
                    }

                    guardarActividad(tipo, titulo, descripcion, fecha, url, archiUrl, archiNombre, trimestreId, adjunInfo);
                    Toast.makeText(requireContext(),tipo + " guardado: " + titulo + " " + adjunInfo, Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancelar", (dialog, which) -> {
                    layoutArchiSelec = null;
                    tvNombreArchivo = null;
                }).show();
    }
    private void cargarTrimestresAct(List<Integer> ids, List<String> nombres) {
        admin = new AdminSQLiteOpenHelper(requireContext(), "BD_Sygemy", null, 1);
        SQLiteDatabase bd_trimesAct = admin.getReadableDatabase();
        Cursor fila = bd_trimesAct.rawQuery("SELECT id, nombre FROM trimestres ORDER BY id", null);

        if (fila.moveToFirst()) {
            do {
                ids.add(fila.getInt(0));
                nombres.add(fila.getString(1));
            } while (fila.moveToNext());
        }
        fila.close();
        bd_trimesAct.close();
    }
    private void guardarActividad( String tipo, String titulo, String descripcion, String fecha, String url, Uri archivoUrl, String archivoNombre, int trimestreId, String adjunInfo) {

        android.util.Log.d("Actividad", "Tipo: " + tipo);
        android.util.Log.d("Actividad", "Título: " + titulo);
        android.util.Log.d("Actividad", "Descripción: " + descripcion);
        android.util.Log.d("Actividad", "Fecha: " + fecha);
        android.util.Log.d("Actividad", "Url: " + (url.isEmpty() ? "ninguno" : url));
        android.util.Log.d("Actividad", "Archivo URL: " + (archivoUrl != null ? archivoUrl.toString() : "ninguno"));

        if (archivoUrl != null) {
            try {
                requireContext().getContentResolver().takePersistableUriPermission(archivoUrl, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        Actividad act = new Actividad( tipo, titulo, descripcion, fecha, url, archivoNombre != null ? archivoNombre : "", archivoUrl != null ? archivoUrl.toString() : "", materia_profe, trimestreId);
        ActividadDao daoAct = new ActividadDao(requireContext());
        boolean cargado = daoAct.insertar(act);

        if (!cargado) {
            Toast.makeText(requireContext(), "Error al guardar la actividad", Toast.LENGTH_SHORT).show();
        }
    }
    private String obtenNomArchiv(Uri uri) {
        String nombArchi = "archivo_desconocido";
        ContentResolver resol = requireContext().getContentResolver();

        try (Cursor cursor = resol.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) nombArchi = cursor.getString(idx);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return nombArchi;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;          // NUEVO
        rvActividades = null;    // NUEVO
        progressActividades = null; // NUEVO
    }
}