package Entidades;

import android.content.ContentValues;

public class Usuarios {

    int id;
    String usuario;
    String contrasenia;
    String rol;

    public Usuarios(String usuario, String contrasenia, String rol){
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public Usuarios(int id, String usuario, String contrasenia, String rol){
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public Usuarios(){}
    public ContentValues Valores() {
        ContentValues valores = new ContentValues();

        valores.put("usuario", usuario);
        valores.put("contrasenia", contrasenia);
        valores.put("rol", rol);

        return valores;
    }
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
