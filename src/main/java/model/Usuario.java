package model;

public class Usuario {
    private String username;
    private String password;
    private String rol;
    private boolean isActive;

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.isActive = true;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}