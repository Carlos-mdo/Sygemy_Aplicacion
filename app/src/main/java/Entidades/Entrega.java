package Entidades;

import android.content.ContentValues;

public class Entrega {
    public int id;
    public int actividadId;
    public int alumnoId;
    public String urlEnt;
    public String archivoNombre;
    public String archivoUrl;
    public String fechaEnt;
    public String coment;
    public Entrega(int actividadId, int alumnoId, String urlEntrega, String archivoNombre, String archivoUrl, String fechaEntrega, String comentario) {
        this.actividadId  = actividadId;
        this.alumnoId     = alumnoId;
        this.urlEnt   = urlEntrega;
        this.archivoNombre = archivoNombre;
        this.archivoUrl   = archivoUrl;
        this.fechaEnt = fechaEntrega;
        this.coment   = comentario;
    }

    public ContentValues Valores() {
        ContentValues cv = new ContentValues();
        cv.put("actividad_id",   actividadId);
        cv.put("alumno_id",      alumnoId);
        cv.put("url_ent",    urlEnt);
        cv.put("archivo_nombre", archivoNombre);
        cv.put("archivo_url",    archivoUrl);
        cv.put("fecha_ent",  fechaEnt);
        cv.put("comentario",     coment);
        return cv;
    }
}
