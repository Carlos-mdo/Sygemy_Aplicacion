package Entidades;

public class EntregaPendiente {
    public int entrega_id, actividad_id, alumno_id, trimestreId;
    public String titulo_Actividad, nombre_Alum, curso_Alum, url_Ent, arch_Nombre, arch_Url, fecha_Ent, comentario, trimestreNombre;
    public Double nota;
    public boolean corregida = false;
    public EntregaPendiente(int entregaId, int actividadId, int alumnoId, String tituloActividad, String nombreAlumno,
      String cursoAlum, String urlEnt, String archivoNombre, String archivoUrl, String fechaEnt, String comentario) {
        this.entrega_id = entregaId;
        this.actividad_id = actividadId;
        this.alumno_id = alumnoId;
        this.titulo_Actividad = tituloActividad;
        this.nombre_Alum = nombreAlumno;
        this.curso_Alum = cursoAlum;
        this.url_Ent = urlEnt;
        this.arch_Nombre = archivoNombre;
        this.arch_Url = archivoUrl;
        this.fecha_Ent = fechaEnt;
        this.comentario = comentario;
    }
}
