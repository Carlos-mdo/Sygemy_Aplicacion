package Entidades;

public class Calificacion {

        public int id, alumno_id, trimestre_id, entrega_id;
        public String descripcion, fecha, materia_cal, nombre_profe, apellido_profe;
        public double nota;

    public Calificacion() {
    }

    public Calificacion(int id, String descripcion, double nota, String fecha, int ent_id) {
        this.id = id;
        this.descripcion = descripcion;
        this.nota = nota;
        this.fecha = fecha;
        this.entrega_id = ent_id;
    }
}
