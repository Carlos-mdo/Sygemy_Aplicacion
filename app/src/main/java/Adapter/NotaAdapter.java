package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Calificacion;

public class NotaAdapter extends ArrayAdapter<Calificacion> {

    public interface OnNotaListener {
        void onEditar(Calificacion c, int posicion);
        void onEliminar(Calificacion c, int posicion);
    }

    private final List<Calificacion> lista;
    private final OnNotaListener listener;

    public NotaAdapter(@NonNull Context context, @NonNull List<Calificacion> lista, OnNotaListener listener) {
        super(context, 0, lista);
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int posicion, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_nota, parent, false);
        }

        Calificacion c = lista.get(posicion);

        TextView tvDescripcionNota = v.findViewById(R.id.tvDescripcionNota);
        TextView tvNotaNota = v.findViewById(R.id.tvNotaNota);
        TextView tvFechaNota = v.findViewById(R.id.tvFechaNota);
        ImageButton btnEditarNota = v.findViewById(R.id.btnEditarNota);
        ImageButton btnEliminarNota = v.findViewById(R.id.btnEliminarNota);

        tvDescripcionNota.setText(c.descripcion);
        tvNotaNota.setText("Nota: " + c.nota);
        tvFechaNota.setText(c.fecha);

        btnEditarNota.setOnClickListener(view -> {
            if (listener != null) { listener.onEditar(c, posicion); }
        });
        btnEliminarNota.setOnClickListener(view -> {
            if (listener != null) { listener.onEliminar(c, posicion); }
        });

        return v;
    }
}