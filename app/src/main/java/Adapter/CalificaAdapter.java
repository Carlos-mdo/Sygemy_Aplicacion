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

import Entidades.Calificacion;

public class CalificaAdapter extends RecyclerView.Adapter<CalificaAdapter.ViewHolder> {
    private final List<Calificacion> lista;
    private final Context context;
    private String profe;

    public CalificaAdapter(List<Calificacion> lista, Context contexto) {
        this.lista = lista;
        this.context = contexto;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup vGroup, int tipo) {
        View v = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.item_calificacion, vGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vHolder, int posicion) {
        Calificacion cal = lista.get(posicion);

        vHolder.txtDescripcion_Calif.setText(cal.descripcion);
        vHolder.txtMateria_Calif.setText(cal.materia_cal != null ? cal.materia_cal : "");
        vHolder.txtNota_Calif.setText("Nota: " + cal.nota);
        vHolder.txtFecha_Calif.setText(cal.fecha);

        if (cal.nombre_profe != null && cal.apellido_profe != null) {
            profe = "Profesor: " + cal.apellido_profe + " " + cal.nombre_profe;
        } else {
            profe = "Profesor: no disponible";
        }
        vHolder.txtProfesor_Calif.setText(profe);
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcion_Calif, txtMateria_Calif, txtNota_Calif, txtFecha_Calif, txtProfesor_Calif;
        ViewHolder(View v) {
            super(v);
            txtDescripcion_Calif = v.findViewById(R.id.txtDescripcion_Calificacion);
            txtMateria_Calif     = v.findViewById(R.id.txtMateria_Calificacion);
            txtNota_Calif        = v.findViewById(R.id.txtNota_Calificacion);
            txtFecha_Calif       = v.findViewById(R.id.txtFecha_Calificacion);
            txtProfesor_Calif    = v.findViewById(R.id.txtProfesor_Calificacion);
        }
    }
}