package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import utils.Paths;


public class AdminProductosController {

    @FXML
    private Button btnAltas;

    @FXML
    private Button btnBajas;

    @FXML
    private Button btnConsultar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnRegresar;

    @FXML
    void altaProducto(ActionEvent event) {

    }

    @FXML
    void bajaProducto(ActionEvent event) {

    }

    @FXML
    void consultarProductos(ActionEvent event) {
        App.app.setScene(Paths.MOSTRAR_PRODUCTOS);
    }

    @FXML
    void modificarProducto(ActionEvent event) {

    }

    @FXML
    void regresar(ActionEvent event) {
        App.app.setScene(Paths.MENU_ADMIN);
    }

}
