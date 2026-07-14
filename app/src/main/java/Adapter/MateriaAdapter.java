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

import Entidades.Materia;
public class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.SubjectViewHolder> {
    private List<Materia> materias = new ArrayList<>();

    public void setMaterias(List<Materia> nuevasMaterias) {
        this.materias = nuevasMaterias != null ? nuevasMaterias : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materia, parent, false);
        return new SubjectViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Materia materia = materias.get(position);

        holder.tvSubjectName.setText(materia.getNombre());

        String profesor = materia.getProfesor();
        holder.tvSubjectTeacher.setText((profesor == null || profesor.trim().isEmpty()) ? "Sin profesor asignado" : profesor);

        holder.tvSubjectStatus.setText(materia.getEstado());
        if (materia.estaAprobada()) {
            holder.tvSubjectStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvSubjectStatus.setTextColor(Color.parseColor("#F9A825"));
        }

        if (materia.getPromedio() != null) {
            holder.tvSubjectGrade.setVisibility(View.VISIBLE);
            holder.tvSubjectGrade.setText(String.format("Promedio: %.1f", materia.getPromedio()));
        } else {
            holder.tvSubjectGrade.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return materias.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName;
        TextView tvSubjectTeacher;
        TextView tvSubjectStatus;
        TextView tvSubjectGrade;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvSubjectTeacher = itemView.findViewById(R.id.tvSubjectTeacher);
            tvSubjectStatus = itemView.findViewById(R.id.tvSubjectStatus);
            tvSubjectGrade = itemView.findViewById(R.id.tvSubjectGrade);
        }
    }
}
