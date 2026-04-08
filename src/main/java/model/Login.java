package model;

public class Login {
    private String user;
    private String password;
    private boolean isLogged = false;

    public Login(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public boolean validarCredenciales(String user, String password) {
        return user.equals(this.user) && password.equals(this.password);
    }
}
