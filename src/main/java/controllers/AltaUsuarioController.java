package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Usuario;
import utils.Alertas;
import utils.Paths;

import java.net.URL;
import java.util.ResourceBundle;

public class AltaUsuarioController implements Initializable {

    @FXML private Button btnCrearUsuario;
    @FXML private Button btnRegresar;

    @FXML private Label lblUsername;
    @FXML private Label lblPassword;
    @FXML private Label lblRol;

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;

    @FXML private ChoiceBox<String> cbRol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbRol.getItems().addAll("ADMIN", "VENDEDOR");
        cbRol.setValue("VENDEDOR");
    }

    @FXML
    void crearUsuario(ActionEvent event) {
        if (!validarCamposNuevos()) return;

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String rol = cbRol.getValue();

        Usuario usuarioTemp = null;
        for (Usuario u : App.app.gestorUsuarios.getListaUsuarios()) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                usuarioTemp = u;
                break;
            }
        }

        if (usuarioTemp == null) {
            usuarioTemp = new Usuario(username, password, rol);
            App.app.gestorUsuarios.getListaUsuarios().add(usuarioTemp);

            Alertas.mostarSuccess("Éxito", "El usuario se ha dado de alta correctamente.");
            limpiarCampos();

        } else {
            if (!usuarioTemp.isActive()) {
                usuarioTemp.setPassword(password);
                usuarioTemp.setRol(rol);
                usuarioTemp.setActive(true);

                Alertas.mostarSuccess("Usuario Reactivado", "El usuario '" + usuarioTemp.getUsername() + "' estaba inactivo y ha sido reactivado.");
                limpiarCampos();
            } else {
                Alertas.mostarWarning("Usuario Registrado", "El nombre de usuario ya está en uso y se encuentra activo. Elige otro.");
            }
        }
    }

    private boolean validarCamposNuevos() {
        if (txtUsername.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty() ||
                cbRol.getValue() == null) {

            Alertas.mostarWarning("Campos Vacíos", "Ningún campo puede estar vacío.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtUsername.clear();
        txtPassword.clear();
        cbRol.setValue("VENDEDOR");
    }

    @FXML
    void regresarMenuAdmin(ActionEvent event) {
        App.app.setScene(Paths.ADMINISTRAR_USUARIOS);
    }
}