package Datos;

import Entidades.Usuarios;

public class SesionDao {

    private static SesionDao instancia = null;
    private Usuarios usuarioActual;

    private SesionDao() { }

    public static SesionDao getInstancia() {
        if (instancia == null) {
            instancia = new SesionDao();
        }
        return instancia;
    }

    public void iniciarSesion(Usuarios usuario) {
        this.usuarioActual = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    public int getUsuarioId() {
        return usuarioActual != null ? usuarioActual.getId() : -1;
    }
}
