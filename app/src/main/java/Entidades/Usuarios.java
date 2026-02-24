package Entidades;

public class Usuarios {

    int id;
    String usuario;
    String contrasenia;
    String rol;

    public void setId(int _id) {
        id = _id;
    }

    public void setUsuario(String _usuario) {
        usuario = _usuario;
    }

    public void setContrasenia(String _contrasenia) {
        contrasenia = _contrasenia;
    }

    public void setRol(String _rol) {
        rol = _rol;
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }
    public String getRol(){
        return rol;
    }
}
