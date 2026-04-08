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
        if (App.login.validarCredenciales(txtUsuario.getText(), txtPassword.getText())) {
            System.out.println("Iniciando sesion...");
            App.login.setLogged(true);
            App.app.setScene(Paths.MENU_ADMIN);
        } else {
            App.login.setLogged(false);
            Alertas.mostarError("CREDENCIALES INVALIDAS", "Usuario o Contraseña incorrectos. Intenta de nuevo");
        }
    }

    @FXML
    void regresar(ActionEvent event) {
        System.out.println("Regresando al menu principal");
        App.app.setScene(Paths.MENU_PRINCIPAL);
    }

}
