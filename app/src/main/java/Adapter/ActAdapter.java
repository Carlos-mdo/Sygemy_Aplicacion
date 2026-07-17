package Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.FileProvider;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Datos.EntregaDao;
import Entidades.Actividad;
import Ventana.Alumno.Fragment_Material;

public class ActAdapter extends RecyclerView.Adapter<ActAdapter.ViewHolder> {

    private final List<Actividad> lista;
    private final Context context;
    private final int alumnoId;
    private final Fragment_Material fragment;

    public ActAdapter(List<Actividad> lista, Context context, int alumnoId, Fragment_Material fragment) {
        this.lista = lista;
        this.context = context;
        this.alumnoId = alumnoId;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup vGroup, int tipo) {
        View v = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.item_actividad, vGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vHolder, int posicion) {
        Actividad act = lista.get(posicion);
        vHolder.tvTipo.setText(act.getTipo());
        vHolder.tvTitulo.setText(act.getTitulo());
        vHolder.tvDescripcion.setText(act.getDescripcion());
        vHolder.tvFecha.setText(act.getFecha());

        if (act.getEnlace() != null && !act.getEnlace().isEmpty()) {
            vHolder.tvAdjunto.setText(act.getEnlace());
            vHolder.tvAdjunto.setVisibility(View.VISIBLE);
            vHolder.tvAdjunto.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(act.getEnlace()));
                vHolder.itemView.getContext().startActivity(intent);
            });
        } else if (act.getArchivoUrl() != null && !act.getArchivoUrl().isEmpty()) {
            vHolder.tvAdjunto.setText(act.getArchivoNombre());
            vHolder.tvAdjunto.setVisibility(View.VISIBLE);
            vHolder.tvAdjunto.setOnClickListener(v -> {
                android.content.Context ctx = vHolder.itemView.getContext();
                java.io.File archivo = new java.io.File(act.getArchivoUrl());

                if (!archivo.exists()) {
                    Toast.makeText(ctx, "El archivo ya no está disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Uri uriArchivo = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", archivo);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uriArchivo, obtenerMimeType(act.getArchivoNombre()));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(ctx, "No hay una app instalada para abrir este tipo de archivo", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ctx, "No se puede abrir el archivo", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            vHolder.tvAdjunto.setVisibility(View.GONE);
        }

        if (context != null && fragment != null) {
            EntregaDao entregaDao = new EntregaDao(context);
            boolean yaEntrego = entregaDao.yaEntrego(act.getId(), alumnoId);

            vHolder.btnEntregar.setVisibility(View.VISIBLE);
            if (yaEntrego) {
                vHolder.btnEntregar.setText("Entregado");
                vHolder.btnEntregar.setEnabled(false);
            } else {
                vHolder.btnEntregar.setText("Entregar");
                vHolder.btnEntregar.setEnabled(true);
                vHolder.btnEntregar.setOnClickListener(v ->
                        fragment.ventanaEntrega(act.getId())
                );
            }
        } else {
            vHolder.btnEntregar.setVisibility(View.GONE);
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

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvTitulo, tvDescripcion, tvFecha, tvAdjunto;
        Button btnEntregar;
        ViewHolder(View v) {
            super(v);
            tvTipo = v.findViewById(R.id.tvTipo);
            tvTitulo = v.findViewById(R.id.tvTituloAct);
            tvDescripcion = v.findViewById(R.id.tvDescripcionAct);
            tvFecha = v.findViewById(R.id.tvFechaAct);
            tvAdjunto = v.findViewById(R.id.tvAdjunto);
            btnEntregar = v.findViewById(R.id.btnEntregar);
        }
    }
}
