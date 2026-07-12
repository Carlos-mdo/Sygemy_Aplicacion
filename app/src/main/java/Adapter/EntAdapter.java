package Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.EntregaPendiente;
public class EntAdapter extends RecyclerView.Adapter<EntAdapter.ViewHolder>{

    public interface OnEntregaClickListener {
        void onEntregaClick(EntregaPendiente entrega);
    }

    private final List<EntregaPendiente> lista;
    private final Context context;
    private final OnEntregaClickListener listener;

    public EntAdapter(List<EntregaPendiente> lista, Context context, OnEntregaClickListener listener) {
        this.lista = lista;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup vGroup, int tipo) {
        View v = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.item_entrega_correcion, vGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vHolder, int posicion) {
        EntregaPendiente ent = lista.get(posicion);
        vHolder.tvTituloActCorreccion.setText(ent.titulo_Actividad);
        vHolder.tvAlumnoCorreccion.setText(ent.nombre_Alum);
        vHolder.tvCursoCorreccion.setText(ent.curso_Alum);
        vHolder.tvFechaCorreccion.setText(ent.fecha_Ent);

        if (ent.corregida && ent.nota != null) {
            vHolder.tvNotaCorreccion.setText("Nota: " + ent.nota);
            vHolder.tvNotaCorreccion.setVisibility(View.VISIBLE);
        } else {
            vHolder.tvNotaCorreccion.setVisibility(View.GONE);
        }
        vHolder.itemView.setOnClickListener(v -> listener.onEntregaClick(ent));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTituloActCorreccion, tvAlumnoCorreccion, tvCursoCorreccion, tvFechaCorreccion, tvNotaCorreccion;
        ViewHolder(View v) {
            super(v);
            tvTituloActCorreccion = v.findViewById(R.id.tvTituloActCorreccion);
            tvAlumnoCorreccion    = v.findViewById(R.id.tvAlumnoCorreccion);
            tvCursoCorreccion     = v.findViewById(R.id.tvCursoCorreccion);
            tvFechaCorreccion     = v.findViewById(R.id.tvFechaCorreccion);
            tvNotaCorreccion      = v.findViewById(R.id.tvNotaCorreccion);
        }
    }
}
