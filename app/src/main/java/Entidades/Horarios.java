package Entidades;

import android.content.ContentValues;

public class Horarios {
    private int idHorario;
    private String curso, materia, dia, horaInicio, horaFin;
    private int profesorId;
    private String profesorNombre;

    public Horarios(String curso, String materia, String dia, String horaInicio, String horaFin, int profesorId) {
        this.curso = curso;
        this.materia = materia;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.profesorId = profesorId;
    }

    public Horarios(int idHorario, String curso, String materia, String dia, String horaInicio, String horaFin,
                    int profesorId, String profesorNombre) {
        this.idHorario = idHorario;
        this.curso = curso;
        this.materia = materia;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.profesorId = profesorId;
        this.profesorNombre = profesorNombre;
    }

    public ContentValues Valores() {
        ContentValues valores = new ContentValues();
        valores.put("curso_hor", curso);
        valores.put("materia_hor", materia);
        valores.put("dia_hor", dia);
        valores.put("horaInicio_hor", horaInicio);
        valores.put("horaFin_hor", horaFin);
        valores.put("profesor_id", profesorId);
        return valores;
    }

    public int getIdHorario() { return idHorario; }
    public String getCurso() { return curso; }
    public String getMateria() { return materia; }
    public String getDia() { return dia; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public int getProfesorId() { return profesorId; }
    public String getProfesorNombre() { return profesorNombre; }
}
