package Entidades;
public class Materia {

    public static final String ESTADO_EN_CURSO = "En curso";
    public static final String ESTADO_APROBADA = "Aprobada";

    private String nombre;
    private String profesor;
    private String estado;
    private Double promedio;

    public Materia(String nombre, String profesor, String estado, Double promedio) {
        this.nombre = nombre;
        this.profesor = profesor;
        this.estado = estado;
        this.promedio = promedio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getEstado() {
        return estado;
    }

    public Double getPromedio() {
        return promedio;
    }

    public boolean estaAprobada() {
        return ESTADO_APROBADA.equals(estado);
    }
}
