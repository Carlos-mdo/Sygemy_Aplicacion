package Entidades;

public class Cursos {

    private String nombreCurso, materia, dias, horaInicio, horaFin;
    private int cantidadAlumnos;

    public Cursos(String nombreCurso, String materia, String dias, String horaInicio, String horaFin, int cantidadAlumnos) {
        this.nombreCurso = nombreCurso;
        this.materia = materia;
        this.dias = dias;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cantidadAlumnos = cantidadAlumnos;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }
    public String getMateria() {
        return materia;
    }
    public String getDias() {
        return dias;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public String getHoraFin() {
        return horaFin;
    }
    public int getCantidadAlumnos() {
        return cantidadAlumnos;
    }
}
