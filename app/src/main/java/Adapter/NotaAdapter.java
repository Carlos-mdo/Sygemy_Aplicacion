package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Calificacion;

public class NotaAdapter extends ArrayAdapter<Calificacion> {

    private Context context;
    private List<Calificacion> lista;
    private OnNotaListener listener;

    public interface OnNotaListener {
        void onEditar(Calificacion calificacion, int posicion);
        void onEliminar(Calificacion calificacion, int posicion);
    }
    public NotaAdapter(Context context, List<Calificacion> lista, OnNotaListener listener) {
        super(context, 0, lista);
        this.context  = context;
        this.lista    = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) { convertView = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false); }

        Calificacion c = lista.get(position);

        TextView txtDescripcion = convertView.findViewById(R.id.txtDescripcion);
        TextView txtNota        = convertView.findViewById(R.id.txtNota);
        TextView txtFecha       = convertView.findViewById(R.id.txtFecha);
        Button   btnEditar      = convertView.findViewById(R.id.btnEditar);
        Button   btnEliminar    = convertView.findViewById(R.id.btnEliminar);

        txtDescripcion.setText(c.descripcion);
        txtNota.setText("Nota: " + c.nota);
        txtFecha.setText("Fecha: " + c.fecha);

        btnEditar.setOnClickListener(v -> {
            if (listener != null) { listener.onEditar(c, position); }
        });

        btnEliminar.setOnClickListener(v -> {
            if (listener != null) { listener.onEliminar(c, position); }
        });

        return convertView;
    }
}