package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.Paths;

public class MenuVentasController {

    @FXML
    void regresarMenuPrincipal(ActionEvent event) {
        App.app.setScene(Paths.MENU_PRINCIPAL);
    }
}
