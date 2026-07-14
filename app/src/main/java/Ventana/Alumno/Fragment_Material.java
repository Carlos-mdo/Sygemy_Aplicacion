package Ventana.Alumno;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Adapter.ActAdapter;
import Datos.ActividadDao;
import Datos.AdminSQLiteOpenHelper;
import Datos.EntregaDao;
import Datos.UsuarioDao;
import Entidades.Actividad;
import Entidades.Entrega;

public class Fragment_Material extends Fragment {

    private int alumId;
    private int trimestreActualId = -1;
    private Uri archiUrl = null;
    private String archiNomb = null;
    private ActivityResultLauncher<String> selectArchi;
    private LinearLayout layArchiEntrega;
    private TextView txtNombArchiEnt;
    private SQLiteDatabase bd;

    public Fragment_Material() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        selectArchi = registerForActivityResult( new ActivityResultContracts.GetContent(),uri -> {
                    if (uri != null) {
                        archiUrl = uri;
                        archiNomb = obtenNombArchiv(uri);
                        if (txtNombArchiEnt != null && layArchiEntrega != null) {
                            txtNombArchiEnt.setText(archiNomb);
                            layArchiEntrega.setVisibility(View.VISIBLE);}
                    }
                }
        );
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__material, container, false);

        RecyclerView rvAct = view.findViewById(R.id.recyclerActividades);
        rvAct.setLayoutManager(new LinearLayoutManager(requireContext()));
        ActividadDao daoAct = new ActividadDao(requireContext());
        List<Actividad> listAct = daoAct.obtenerTodas();
        alumId = UsuarioDao.obtenerAlumnoId(requireContext());

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "BD_Sygemy", null, 1);
        bd = admin.getWritableDatabase();

        trimestreActualId = obtenerTrimestreIdActual(bd);

        List<Actividad> listActFiltrada = new ArrayList<>();
        for (Actividad act : listAct) {
            if (act.getTrimestreId() == trimestreActualId) {
                listActFiltrada.add(act);
            }
        }
        rvAct.setAdapter(new ActAdapter(listActFiltrada, requireContext(), alumId, this));

        return view;
    }
    public void ventanaEntrega(int actividadId) {
        View vEntrega = LayoutInflater.from(requireContext()).inflate(R.layout.item_entrega, null);
        EditText etUrl = vEntrega.findViewById(R.id.etEnlaceEntrega);
        EditText etComent = vEntrega.findViewById(R.id.etComentarioEntrega);
        Button btnAdjunt = vEntrega.findViewById(R.id.btnAdjuntarEntrega);
        layArchiEntrega = vEntrega.findViewById(R.id.layoutArchivoEntrega);
        txtNombArchiEnt = vEntrega.findViewById(R.id.tvNombreArchivoEntrega);
        ImageButton btnEliminar = vEntrega.findViewById(R.id.btnEliminarArchivoEntrega);

        btnAdjunt.setOnClickListener(v -> selectArchi.launch("*/*"));
        btnEliminar.setOnClickListener(v -> {
            archiUrl = null;
            archiNomb = null;
            layArchiEntrega.setVisibility(View.GONE);
        });

        new AlertDialog.Builder(requireContext()).setTitle("Entregar actividad")
                .setView(vEntrega)
                .setPositiveButton("Entregar", (dialog, which) -> {
                    String url = etUrl.getText().toString().trim();
                    String coment = etComent.getText().toString().trim();

                    if (url.isEmpty() && archiUrl == null) {
                        Toast.makeText(requireContext(),"AgregÃ¡ una url o un archivo", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (archiUrl != null) {
                        try {
                            requireContext().getContentResolver().takePersistableUriPermission(archiUrl, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                    String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
                    Entrega ent = new Entrega(actividadId, alumId, url, archiNomb != null ? archiNomb : "", archiUrl != null ? archiUrl.toString() : "", fecha, coment);

                    EntregaDao daoEnt = new EntregaDao(requireContext());
                    boolean estado = daoEnt.insertar(ent);

                    Toast.makeText(requireContext(), estado ? "Entrega realizada" : "Error al entregar", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancelar", (d, w) -> {
                    layArchiEntrega = null;
                    txtNombArchiEnt = null;
                }).show();
    }
    private int obtenerNumeroTrimestre() {
        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.MILLISECOND, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.HOUR_OF_DAY, 0);

        Calendar inicio1 = crearFecha(2026, Calendar.MARCH, 3);
        Calendar fin1    = crearFecha(2026, Calendar.MAY, 30);
        Calendar inicio2 = crearFecha(2026, Calendar.JUNE, 1);
        Calendar fin2    = crearFecha(2026, Calendar.SEPTEMBER, 15);
        Calendar inicio3 = crearFecha(2026, Calendar.SEPTEMBER, 16);
        Calendar fin3    = crearFecha(2026, Calendar.DECEMBER, 18);

        if (!hoy.before(inicio1) && !hoy.after(fin1)) return 1;
        if (!hoy.before(inicio2) && !hoy.after(fin2)) return 2;
        if (!hoy.before(inicio3) && !hoy.after(fin3)) return 3;

        return hoy.before(inicio1) ? 1 : 3;
    }
    private Calendar crearFecha(int anio, int mes, int dia) {
        Calendar c = Calendar.getInstance();
        c.set(anio, mes, dia, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }
    private int obtenerTrimestreIdActual(SQLiteDatabase bd) {
        int numero = obtenerNumeroTrimestre();
        String nombre = numero + "° Trimestre";
        int id = -1;
        try (Cursor cursor = bd.rawQuery("SELECT id FROM trimestres WHERE nombre = ?", new String[]{nombre})) {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
        }
        return id;
    }

    private String obtenNombArchiv(Uri uri) {
        String nombArchi = "archivo";
        try (Cursor curArchi = requireContext().getContentResolver().query(uri, null, null, null, null)) {
            if (curArchi != null && curArchi.moveToFirst()) {
                int idx = curArchi.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) { nombArchi = curArchi.getString(idx); }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return nombArchi;
    }
}