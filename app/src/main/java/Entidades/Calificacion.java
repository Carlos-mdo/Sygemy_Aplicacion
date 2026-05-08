package Entidades;

public class Calificacion {

    public int id;
    public String descripcion;
    public double nota;
    public String fecha;

    public Calificacion() {
    }

    public Calificacion(int id, String descripcion, double nota, String fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.nota = nota;
        this.fecha = fecha;
    }
}
