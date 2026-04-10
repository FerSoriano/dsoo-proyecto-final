package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.Alertas;
import utils.Paths;

public class MenuLoginController {

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegresar;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsuario;

    @FXML
    void login(ActionEvent event) {

        boolean loginExitoso = App.app.sesionActual.iniciarSesion(
                txtUsuario.getText(),
                txtPassword.getText(),
                App.app.gestorUsuarios.getListaUsuarios()
        );

        if (loginExitoso) {
            String rolUsuario = App.app.sesionActual.getUsuarioActual().getRol();

            if (rolUsuario.equalsIgnoreCase(App.app.moduloDestino)) {

                if (App.app.moduloDestino.equals("ADMIN")) {
                    App.app.setScene(Paths.MENU_ADMIN);
                } else if (App.app.moduloDestino.equals("VENDEDOR")) {
                    App.app.setScene(Paths.MENU_VENTAS);
                }

            } else {
                Alertas.mostarError("ACCESO DENEGADO", "Tu usuario es tipo " + rolUsuario + " y no tienes permisos para acceder a este módulo.");
                App.app.sesionActual.cerrarSesion();
            }

        } else {
            Alertas.mostarError("CREDENCIALES INVÁLIDAS", "Usuario o Contraseña incorrectos o usuario inactivo.");
        }
    }

    @FXML
    void regresar(ActionEvent event) {
        System.out.println("Regresando al menu principal");
        App.app.setScene(Paths.MENU_PRINCIPAL);
    }

}
