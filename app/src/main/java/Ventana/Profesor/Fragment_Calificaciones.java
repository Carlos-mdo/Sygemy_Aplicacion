package Ventana.Profesor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.gestion_escolar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

import Adapter.EntAdapter;
import Adapter.NotaAdapter;
import Datos.AdminSQLiteOpenHelper;
import Datos.EntregaDao;
import Entidades.Calificacion;
import Entidades.EntregaPendiente;

public class Fragment_Calificaciones extends Fragment {
        private Spinner spinnerCurso, spinnerAlumno, spinnerTrimestre;
        private ListView listViewNotas;
        private Button btnAgregarNota;
        private RecyclerView rvEntregasPendientes;
        private TextView tvSinEntregas;
        private List<Calificacion> listaNotas = new ArrayList<>();
        private List<EntregaPendiente> listaEntregasPendientes = new ArrayList<>();
        private List<Integer> listaAlumnos = new ArrayList<>();
        private List<Integer> listaTrimestres = new ArrayList<>();
        private SQLiteDatabase db;
        private boolean cargandoAlumnos = false;
        private String desc, valorNota, materiaProfe;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment__calificaciones, container, false);
            spinnerCurso = view.findViewById(R.id.spinnerCurso);
            spinnerAlumno = view.findViewById(R.id.spinnerAlumno);
            spinnerTrimestre= view.findViewById(R.id.spinnerTrimestre);
            listViewNotas = view.findViewById(R.id.listNotas);
            btnAgregarNota = view.findViewById(R.id.btnAgregarNota);
            rvEntregasPendientes = view.findViewById(R.id.rvEntregasPendientes);
            tvSinEntregas = view.findViewById(R.id.tvSinEntregas);
            rvEntregasPendientes.setLayoutManager(new LinearLayoutManager(requireContext()));

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "BD_Sygemy", null, 1);
            db = admin.getWritableDatabase();
            materiaProfe = Datos.UsuarioDao.obtenerMateria(requireContext());

            cargarTrimestres();
            cargarCursos();

            spinnerCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    cargarAlumnos();
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
            spinnerAlumno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (!cargandoAlumnos) {
                        cargarNotas();
                        cargarEntregasPendientes();
                    }
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
            spinnerTrimestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    cargarNotas();
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });

            btnAgregarNota.setOnClickListener(v -> insertarNota());
            return view;
        }
        private void cargarCursos() {
            List<String> cursos = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT DISTINCT curso_alum FROM alumnos ORDER BY curso_alum", null);

            if (cursor.moveToFirst()) {
                do { cursos.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();

            if (cursos.isEmpty()) { cursos.add("Sin cursos disponibles"); }

            ArrayAdapter<String> arrayAdap = new ArrayAdapter<>( getContext(), android.R.layout.simple_spinner_item, cursos );
            arrayAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCurso.setAdapter(arrayAdap);
        }
        private void cargarAlumnos() {
            cargandoAlumnos = true;
            List<String> alumnos = new ArrayList<>();
            listaAlumnos.clear();

            String cursoSeleccionado = (String) spinnerCurso.getSelectedItem();
            if (cursoSeleccionado == null || cursoSeleccionado.equals("Sin cursos disponibles")) {
                cargandoAlumnos = false;
                return;
            }

            Cursor cursor = db.rawQuery("SELECT id, apellido_alum || ' ' || nombre_alum " + "FROM alumnos WHERE curso_alum = ? ORDER BY apellido_alum",
                    new String[]{ cursoSeleccionado });

            if (cursor.moveToFirst()) {
                do {
                    listaAlumnos.add(cursor.getInt(0));
                    alumnos.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();

            if (alumnos.isEmpty()) { alumnos.add("Sin alumnos"); }

            ArrayAdapter<String> adapter = new ArrayAdapter<>( getContext(), android.R.layout.simple_spinner_item, alumnos );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAlumno.setAdapter(adapter);

            cargandoAlumnos = false;
            cargarNotas();
            cargarEntregasPendientes();
        }

        private void cargarTrimestres() {
            List<String> trimestres = new ArrayList<>();

            Cursor cursor = db.rawQuery(
                    "SELECT id, nombre FROM trimestres ORDER BY id", null);

            if (cursor.moveToFirst()) {
                do {
                    listaTrimestres.add(cursor.getInt(0));
                    trimestres.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>( getContext(), android.R.layout.simple_spinner_item, trimestres );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTrimestre.setAdapter(adapter);
        }
        private void cargarNotas() {
            listaNotas.clear();
            int posAlumno    = spinnerAlumno.getSelectedItemPosition();
            int posTrimestre = spinnerTrimestre.getSelectedItemPosition();

            if (listaAlumnos.isEmpty() || posAlumno < 0 || posAlumno >= listaAlumnos.size()) {
                actualizarListView();
                return;
            }
            if (listaTrimestres.isEmpty() || posTrimestre < 0 || posTrimestre >= listaTrimestres.size()) {
                actualizarListView();
                return;
            }

            int alumnoId    = listaAlumnos.get(posAlumno);
            int trimestreId = listaTrimestres.get(posTrimestre);

            Cursor cursor = db.rawQuery(
                    "SELECT id, descripcion, nota, fecha, entrega_id FROM calificaciones " +
                            "WHERE alumno_id = ? AND trimestre_id = ? AND materia_cal = ? ORDER BY fecha",
                    new String[]{ String.valueOf(alumnoId), String.valueOf(trimestreId), materiaProfe });

            if (cursor.moveToFirst()) {
                do {
                    Calificacion c = new Calificacion();
                    c.id = cursor.getInt(0);
                    c.descripcion = cursor.getString(1);
                    c.nota = cursor.getDouble(2);
                    c.fecha = cursor.getString(3);
                    c.entrega_id = cursor.getInt(4);
                    listaNotas.add(c);
                } while (cursor.moveToNext());
            }
            cursor.close();

            actualizarListView();
        }
        private void actualizarListView() {

            if (listaNotas.isEmpty()) {
                Toast.makeText(getContext(), "No hay notas para mostrar", Toast.LENGTH_SHORT).show();
            }

            NotaAdapter adapter = new NotaAdapter(getContext(), listaNotas, new NotaAdapter.OnNotaListener() {
                @Override
                public void onEditar(Calificacion c, int posicion) { editarListView(c); }
                @Override
                public void onEliminar(Calificacion c, int posicion) { new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar nota").setMessage("¿ Eliminar " + c.descripcion + "?")
                        .setPositiveButton("Sí", (dialog, which) -> {

                            db.delete("calificaciones", "id = ?", new String[]{ String.valueOf(c.id) });
                            listaNotas.remove(posicion);
                            cargarNotas();
                        }) .setNegativeButton("Cancelar", null).show();
                }
            }
            );
            listViewNotas.setAdapter(adapter);
        }
        private void cargarEntregasPendientes() {
            int posAlumno = spinnerAlumno.getSelectedItemPosition();

            if (listaAlumnos.isEmpty() || posAlumno < 0 || posAlumno >= listaAlumnos.size()) {
                listaEntregasPendientes = new ArrayList<>();
                actualizarListaEntregas();
                return;
            }

            int alumnoId = listaAlumnos.get(posAlumno);
            EntregaDao daoEnt = new EntregaDao(requireContext());
            listaEntregasPendientes = daoEnt.obtenerPendientesAlumno(materiaProfe, alumnoId);
            actualizarListaEntregas();
        }

        private void actualizarListaEntregas() {
            if (listaEntregasPendientes.isEmpty()) {
                tvSinEntregas.setVisibility(View.VISIBLE);
                rvEntregasPendientes.setVisibility(View.GONE);
            } else {
                tvSinEntregas.setVisibility(View.GONE);
                rvEntregasPendientes.setVisibility(View.VISIBLE);
            }

            EntAdapter adapter = new EntAdapter(listaEntregasPendientes, requireContext(), this::abrirCorreccionEntrega);
            rvEntregasPendientes.setAdapter(adapter);
        }
        private void abrirCorreccionEntrega(EntregaPendiente entrega) {
            LinearLayout layout = new LinearLayout(requireContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 20, 40, 10);

            TextView tvActividad = new TextView(requireContext());
            tvActividad.setText("Actividad: " + entrega.titulo_Actividad);

            TextView tvTrimestre = new TextView(requireContext());
            tvTrimestre.setText("Trimestre: " + entrega.trimestreNombre);

            TextView tvComentario = new TextView(requireContext());
            tvComentario.setText("Comentario: " +
                    (entrega.comentario != null && !entrega.comentario.isEmpty() ? entrega.comentario : "Sin comentario"));

            TextView tvAdjunto = new TextView(requireContext());
            boolean tieneUrl     = entrega.url_Ent != null && !entrega.url_Ent.isEmpty();
            boolean tieneArchivo = entrega.arch_Url != null && !entrega.arch_Url.isEmpty();

            if (tieneUrl) {
                tvAdjunto.setText("Enlace: " + entrega.url_Ent);
                tvAdjunto.setOnClickListener(v ->
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entrega.url_Ent))));
            } else if (tieneArchivo) {
                tvAdjunto.setText("Archivo: " + entrega.arch_Nombre);
                tvAdjunto.setOnClickListener(v -> {
                    try {
                        Uri uri = Uri.parse(entrega.arch_Url);
                        String mime = requireContext().getContentResolver().getType(uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, mime != null ? mime : "*/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "No se puede abrir el archivo", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                tvAdjunto.setText("Sin adjunto");
            }

            EditText etNotaEntrega = new EditText(requireContext());
            etNotaEntrega.setHint("Nota (ej: 8.5)");
            etNotaEntrega.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            layout.addView(tvActividad);
            layout.addView(tvTrimestre);
            layout.addView(tvComentario);
            layout.addView(tvAdjunto);
            layout.addView(etNotaEntrega);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Calificar entrega")
                    .setView(layout)
                    .setPositiveButton("Guardar nota", (dialog, which) -> {
                        String valorNotaEnt = etNotaEntrega.getText().toString().trim();
                        if (valorNotaEnt.isEmpty()) {
                            Toast.makeText(requireContext(), "Ingrese una nota", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double nota;
                        try {
                            nota = Double.parseDouble(valorNotaEnt);
                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(), "Nota inválida", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        ContentValues values = new ContentValues();
                        values.put("alumno_id",    entrega.alumno_id);
                        values.put("trimestre_id", entrega.trimestreId);
                        values.put("descripcion",  entrega.titulo_Actividad);
                        values.put("nota",         nota);
                        values.put("fecha",        fecha);
                        values.put("materia_cal",  materiaProfe);
                        values.put("entrega_id",   entrega.entrega_id);

                        long resultado = db.insert("calificaciones", null, values);

                        if (resultado != -1) {
                            Toast.makeText(requireContext(), "Entrega calificada", Toast.LENGTH_SHORT).show();
                            cargarEntregasPendientes();
                            cargarNotas();
                        } else {
                            Toast.makeText(requireContext(), "Error al guardar la nota", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
        private void editarListView(Calificacion c) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Editar Nota");

            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 20, 40, 10);

            EditText etDescripcion = new EditText(getContext());
            etDescripcion.setHint("Descripción");
            etDescripcion.setText(c.descripcion);

            EditText etNota = new EditText(getContext());
            etNota.setHint("Nota");
            etNota.setText(String.valueOf(c.nota));
            etNota.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            layout.addView(etDescripcion);
            layout.addView(etNota);
            builder.setView(layout);

            builder.setPositiveButton("Guardar", (dialog, which) -> {
                desc = etDescripcion.getText().toString().trim();
                valorNota = etNota.getText().toString().trim();

                if (desc.isEmpty() || valorNota.isEmpty()) {
                    Toast.makeText(getContext(), "Completá todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues valorEditable = new ContentValues();
                valorEditable.put("descripcion", desc);
                valorEditable.put("nota", Double.parseDouble(valorNota));

                db.update("calificaciones", valorEditable, "id = ?", new String[]{ String.valueOf(c.id) });

                cargarNotas();
            });
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        }
        private void insertarNota() {
            if (listaAlumnos.isEmpty()) { Toast.makeText(getContext(), "No hay alumnos disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Nueva Nota");
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 20, 40, 10);

            EditText etDescripcion = new EditText(getContext());
            etDescripcion.setHint("Descripción (ej: Parcial 1)");
            EditText etNota = new EditText(getContext());
            etNota.setHint("Nota (ej: 8.5)");
            etNota.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            layout.addView(etDescripcion);
            layout.addView(etNota);
            builder.setView(layout);

            builder.setPositiveButton("Guardar", (dialog, which) -> {
                desc = etDescripcion.getText().toString().trim();
                valorNota  = etNota.getText().toString().trim();

                if (desc.isEmpty() || valorNota.isEmpty()) {
                    Toast.makeText(getContext(), "Completá todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                double notaDouble;
                try { notaDouble = Double.parseDouble(valorNota);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Nota inválida", Toast.LENGTH_SHORT).show();
                    return;
                }

                int alumnoId    = listaAlumnos.get(spinnerAlumno.getSelectedItemPosition());
                int trimestreId = listaTrimestres.get(spinnerTrimestre.getSelectedItemPosition());
                String fecha    = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                ContentValues values = new ContentValues();
                values.put("descripcion",  desc);
                values.put("nota",         notaDouble);
                values.put("fecha",        fecha);
                values.put("alumno_id",    alumnoId);
                values.put("trimestre_id", trimestreId);
                values.put("materia_cal", materiaProfe);

                long resultado = db.insert("calificaciones", null, values);
                if (resultado != -1) {
                    Toast.makeText(getContext(), "La nota fue guardada", Toast.LENGTH_SHORT).show();
                    cargarNotas();
                } else { Toast.makeText(getContext(), "Error al guardar la nota", Toast.LENGTH_SHORT).show();  }
            });

            builder.setNegativeButton("Cancelar", null);
            builder.show();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
}