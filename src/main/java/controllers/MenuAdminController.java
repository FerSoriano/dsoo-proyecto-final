package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import utils.Paths;

public class MenuAdminController {

    @FXML
    private Button btnAdminProductos;

    @FXML
    private Button btnAdminUsuarios;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnCorteCaja;

    @FXML
    private Button btnRegresar;

    @FXML
    void adminProductos(ActionEvent event) {
        App.app.setScene(Paths.ADMINISTRAR_PRODUCTOS);
    }

    @FXML
    void adminUsuarios(ActionEvent event) {
        System.out.println("TODO: Administrar Usuarios");
//        App.app.setScene(Paths.ADMIN_USUARIOS);
    }

    @FXML
    void mostrarCorteCajaGeneral(ActionEvent event) {
        System.out.println("TODO: Corte de caja");
        //        App.app.setScene(Paths.CORTE_CAJA);
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        App.admin.setLogged(false);
        App.app.setScene(Paths.MENU_PRINCIPAL);
    }

    @FXML
    void regresar(ActionEvent event) {
        App.app.setScene(Paths.MENU_PRINCIPAL);
    }

}
