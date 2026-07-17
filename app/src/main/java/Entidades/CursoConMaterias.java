package Entidades;

import java.util.List;

public class CursoConMaterias {
    private String nombreCurso;
    private int cantidadAlumnos;
    private List<String> materiasDelGrado;    // currícula completa del curso (desde materias_grado)
    private List<String> materiasDelProfesor; // lo que ESTE profesor dicta en este curso
    private String horarioResumen;

    public CursoConMaterias(String nombreCurso, int cantidadAlumnos, List<String> materiasDelGrado,
                            List<String> materiasDelProfesor, String horarioResumen) {
        this.nombreCurso = nombreCurso;
        this.cantidadAlumnos = cantidadAlumnos;
        this.materiasDelGrado = materiasDelGrado;
        this.materiasDelProfesor = materiasDelProfesor;
        this.horarioResumen = horarioResumen;
    }

    public String getNombreCurso() { return nombreCurso; }
    public int getCantidadAlumnos() { return cantidadAlumnos; }
    public List<String> getMateriasDelGrado() { return materiasDelGrado; }
    public List<String> getMateriasDelProfesor() { return materiasDelProfesor; }
    public String getHorarioResumen() { return horarioResumen; }
}
