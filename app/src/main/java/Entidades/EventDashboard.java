package Entidades;

public class EventDashboard {
    public static final String TIPO_EXAMEN = "Examen";
    public static final String TIPO_ENTREGA = "Entrega pendiente";

    private String titulo;
    private String materia;
    private String fecha;
    private String tipo;

    public EventDashboard(String titulo, String materia, String fecha, String tipo) {
        this.titulo = titulo;
        this.materia = materia;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMateria() {
        return materia;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean esExamen() {
        return TIPO_EXAMEN.equals(tipo);
    }
}
