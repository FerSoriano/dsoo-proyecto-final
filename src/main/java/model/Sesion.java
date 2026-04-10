package model;

import java.util.ArrayList;

public class Sesion {
    private Usuario usuarioActual = null;

    public boolean iniciarSesion(String username, String password, ArrayList<Usuario> listaUsuarios) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equals(username) &&
                    u.getPassword().equals(password) &&
                    u.isActive()) {

                this.usuarioActual = u;
                return true;
            }
        }
        return false;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public boolean isLogged() {
        return this.usuarioActual != null;
    }

    public Usuario getUsuarioActual() {
        return this.usuarioActual;
    }
}