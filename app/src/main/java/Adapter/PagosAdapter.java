package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicacion.gestion_escolar.R;

import java.util.ArrayList;
import java.util.List;

import Entidades.Alumno;
import Servicios.ServicioAdmin;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {

    private List<Alumno> listaOriginal;
    private List<Alumno> listaAlumnos;
    private ServicioAdmin servicio;

    public PagosAdapter(List<Alumno> listaAlumnos, ServicioAdmin servicio) {
        this.listaAlumnos = new ArrayList<>(listaAlumnos);
        this.listaOriginal = new ArrayList<>(listaAlumnos);
        this.servicio = servicio;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);

        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Alumno alumno = listaAlumnos.get(position);

        holder.txtNombre.setText(alumno.getNombre() + " " + alumno.getApellido());

        holder.txtCurso.setText("Curso: " + alumno.getCurso());

        holder.txtCuotas.setText("Cuotas pagadas: " + alumno.getCuotasPagadas() + "/12");

        holder.txtCantidad.setText(String.valueOf(alumno.getCuotasPagadas()));

        holder.txtValorCuota.setText("Valor cuota: $" + obtenerValorCuota(alumno.getCurso()));

        holder.btnMas.setOnClickListener(v -> {

            int cuotas = alumno.getCuotasPagadas();

            if (cuotas < 12) {
                cuotas++;

                alumno.setCuotasPagadas(cuotas);

                holder.txtCantidad.setText(String.valueOf(cuotas));
                holder.txtCuotas.setText("Cuotas pagadas: " + cuotas + "/12");
            }
        });

        holder.btnMenos.setOnClickListener(v -> {

            int cuotas = alumno.getCuotasPagadas();

            if (cuotas > 0) {
                cuotas--;

                alumno.setCuotasPagadas(cuotas);

                holder.txtCantidad.setText(String.valueOf(cuotas));
                holder.txtCuotas.setText("Cuotas pagadas: " + cuotas + "/12");
            }
        });

        holder.btnConfirmar.setOnClickListener(v -> {

            servicio.actualizarCuotas(
                    alumno.getId(),
                    alumno.getCuotasPagadas()
            );

            Toast.makeText(
                    v.getContext(),
                    "Pago actualizado correctamente",
                    Toast.LENGTH_SHORT
            ).show();
        });

    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtCurso;
        TextView txtCuotas;
        TextView txtValorCuota;
        TextView txtCantidad;

        ImageButton btnMas;
        ImageButton btnMenos;

        View btnConfirmar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCurso = itemView.findViewById(R.id.txtCurso);
            txtCuotas = itemView.findViewById(R.id.txtCuotas);
            txtValorCuota = itemView.findViewById(R.id.txtValorCuota);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);

            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);

            btnConfirmar = itemView.findViewById(R.id.btnConfirmar);
        }
    }

    private int obtenerValorCuota(String curso) {

        switch (curso) {

            case "1":
                return 20000;

            case "2":
                return 30000;

            case "3":
                return 40000;

            case "4":
                return 50000;

            case "5":
                return 60000;

            case "6":
                return 70000;

            default:
                return 0;
        }
    }

    public void filtrar(String texto, String curso) {

        listaAlumnos.clear();

        for (Alumno alumno : listaOriginal) {

            boolean coincideBusqueda =
                    alumno.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                            alumno.getApellido().toLowerCase().contains(texto.toLowerCase()) ||
                            alumno.getDni().contains(texto);

            boolean coincideCurso =
                    curso.equals("Todos") ||
                            alumno.getCurso().equals(curso);

            if (coincideBusqueda && coincideCurso) {
                listaAlumnos.add(alumno);
            }
        }

        notifyDataSetChanged();
    }
}