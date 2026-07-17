package Entidades;

import java.util.List;

public class MateriaSeccion {
    private String materia;
    private List<Alumno> alumnos;
    private boolean esMateriaDelProfesor;

    public MateriaSeccion(String materia, List<Alumno> alumnos, boolean esMateriaDelProfesor) {
        this.materia = materia;
        this.alumnos = alumnos;
        this.esMateriaDelProfesor = esMateriaDelProfesor;
    }

    public String getMateria() { return materia; }
    public List<Alumno> getAlumnos() { return alumnos; }
    public boolean isEsMateriaDelProfesor() { return esMateriaDelProfesor; }
}
