package Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.ArrayList;
import java.util.List;

import Entidades.EventDashboard;

public class DashEventAdapter extends RecyclerView.Adapter<DashEventAdapter.EventoViewHolder> {

    private List<EventDashboard> eventos = new ArrayList<>();

    public void setEventos(List<EventDashboard> nuevosEventos) {
        this.eventos = nuevosEventos != null ? nuevosEventos : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_evento, parent, false);
        return new EventoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        EventDashboard evento = eventos.get(position);

        holder.tvEventoTitulo.setText(evento.getTitulo());
        holder.tvEventoMateria.setText(evento.getMateria() + " . " + evento.getProfesor());
        holder.tvEventoFecha.setText(evento.getFecha());

        int color = evento.esExamen() ? Color.parseColor("#D32F2F") : Color.parseColor("#F9A825");
        holder.viewIndicadorTipo.setBackgroundColor(color);
        holder.tvEventoFecha.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        View viewIndicadorTipo;
        TextView tvEventoTitulo;
        TextView tvEventoMateria;
        TextView tvEventoFecha;

        EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            viewIndicadorTipo = itemView.findViewById(R.id.viewIndicadorTipo);
            tvEventoTitulo = itemView.findViewById(R.id.tvEventoTitulo);
            tvEventoMateria = itemView.findViewById(R.id.tvEventoMateria);
            tvEventoFecha = itemView.findViewById(R.id.tvEventoFecha);
        }
    }

}
