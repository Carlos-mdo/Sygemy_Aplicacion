package Entidades;

public class Alumno {
    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private String genero;
    private String curso;
    private int usuarioId;
    private int cuotasPagadas;


    public Alumno(int id, String dni, String nombre, String apellido, String genero, String curso, int usuarioId, int cuotasPagadas) {

        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.curso = curso;
        this.usuarioId = usuarioId;
        this.cuotasPagadas = cuotasPagadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public int getCuotasPagadas() {
        return cuotasPagadas;
    }

    public void setCuotasPagadas(int cuotasPagadas) {
        this.cuotasPagadas = cuotasPagadas;
    }
}

