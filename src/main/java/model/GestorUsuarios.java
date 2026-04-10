package model;

import java.util.ArrayList;

public class GestorUsuarios {

    private ArrayList<Usuario> listaUsuarios;

    public GestorUsuarios() {
        this.listaUsuarios = new ArrayList<>();
        cargarUsuariosPrueba();
    }

    private void cargarUsuariosPrueba() {
        listaUsuarios.add(new Usuario("admin", "123", "ADMIN"));
        listaUsuarios.add(new Usuario("vend1", "123", "VENDEDOR"));
        listaUsuarios.add(new Usuario("vend2", "123", "VENDEDOR"));
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

     public void agregarUsuario(Usuario u) {
        listaUsuarios.add(u);
     }

     public void darDeBajaUsuario(String username) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                u.setActive(false);
            }
        }
     }
}