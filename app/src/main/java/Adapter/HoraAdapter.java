package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.List;

import Entidades.Horarios;

public class HoraAdapter extends  RecyclerView.Adapter<HoraAdapter.HorarioViewHolder> {

    private List<Horarios> listaHorarios;

    public HoraAdapter(List<Horarios> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horario, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        Horarios h = listaHorarios.get(position);
        holder.tvMateria.setText(h.getMateria());
        holder.tvDia.setText(h.getDia());
        holder.tvHora.setText(h.getHoraInicio() + " - " + h.getHoraFin());
        holder.tvDocente.setText(h.getProfesorNombre());
    }

    @Override
    public int getItemCount() {
        return listaHorarios.size();
    }

    public void actualizarLista(List<Horarios> nuevaLista) {
        this.listaHorarios = nuevaLista;
        notifyDataSetChanged();
    }

    public static class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvMateria, tvDia, tvHora, tvDocente;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMateria = itemView.findViewById(R.id.tvMateria);
            tvDia = itemView.findViewById(R.id.tvDia);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvDocente = itemView.findViewById(R.id.tvDocente);
        }
    }
}
