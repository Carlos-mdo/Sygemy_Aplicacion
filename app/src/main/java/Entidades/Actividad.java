package Entidades;

import android.content.ContentValues;

public class Actividad {
    private int id_act;
    private String tipo_act, titulo_act, descripcion_act, fecha_act, enlace_act, archivoNombre_act, archivoUrl_act, materia_act;
    private int trimestreId_act, profesorId_act;
    public Actividad(String tipo, String titulo, String descripcion, String fecha, String enlace, String archivoNombre, String archivoUrl, String materia, int trimestreId, int profesorId) {
        this.tipo_act = tipo;
        this.titulo_act = titulo;
        this.descripcion_act = descripcion;
        this.fecha_act = fecha;
        this.enlace_act = enlace;
        this.archivoNombre_act = archivoNombre;
        this.archivoUrl_act = archivoUrl;
        this.materia_act = materia;
        this.trimestreId_act = trimestreId;
        this.profesorId_act = profesorId;
    }
    public Actividad(int id, String tipo, String titulo, String descripcion, String fecha, String enlace, String archivoNombre, String archivoUri, String materia, int trimestreId, int profesorId) {
        this(tipo, titulo, descripcion, fecha, enlace, archivoNombre, archivoUri, materia, trimestreId, profesorId);
        this.id_act = id;
    }

    public ContentValues Valores() {
        ContentValues cvValores = new ContentValues();
        cvValores.put("tipo_act", tipo_act);
        cvValores.put("titulo_act", titulo_act);
        cvValores.put("descripcion_act", descripcion_act);
        cvValores.put("fecha_act", fecha_act);
        cvValores.put("url_act", enlace_act);
        cvValores.put("archivo_nombre", archivoNombre_act);
        cvValores.put("archivo_url", archivoUrl_act);
        cvValores.put("materia_act", materia_act);
        cvValores.put("trimestre_id", trimestreId_act);
        cvValores.put("profesor_id", profesorId_act);
        return cvValores;
    }

    public int getId() { return id_act; }
    public String getTipo() { return tipo_act; }
    public String getTitulo() { return titulo_act; }
    public String getDescripcion() { return descripcion_act; }
    public String getFecha() { return fecha_act; }
    public String getEnlace() { return enlace_act; }
    public String getArchivoNombre() { return archivoNombre_act; }
    public String getArchivoUrl() { return archivoUrl_act; }
    public String getMateria() { return materia_act; }
    public int getTrimestreId() { return trimestreId_act; }
    public int getProfesorId(){ return profesorId_act;  }
}
