package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.Paths;

public class MenuAdminController {
    @FXML
    void regresar(ActionEvent event) {
        System.out.println("Regresando al menu principal");
        App.app.setScene(Paths.MENU_PRINCIPAL);
    }
}
