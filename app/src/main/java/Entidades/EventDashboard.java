package Entidades;

public class EventDashboard {
    public static final String TIPO_EXAMEN = "Examen";
    public static final String TIPO_ENTREGA = "Entrega pendiente";

    private String titulo;
    private String materia;
    private String fecha;
    private String tipo;
    private String profesor;

    public EventDashboard(String titulo, String materia, String fecha, String tipo, String profesor) {
        this.titulo = titulo;
        this.materia = materia;
        this.fecha = fecha;
        this.tipo = tipo;
        this.profesor = profesor;
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

    public String getProfesor() { return profesor;}
    public boolean esExamen() {
        return TIPO_EXAMEN.equals(tipo);
    }
}
