package controllers;

import application.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import utils.Paths;
import utils.Alertas;
import java.util.Optional;

public class MenuPrincipalController {

    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnVentas;

    @FXML
    private Button btnSalir;

    @FXML
    void mostrarMenuAdmin(ActionEvent event) {
        System.out.println("Mostrando Menu Admin");
        if (!App.admin.isLogged()) {
            App.app.setScene(Paths.MENU_LOGIN);
        } else {
            App.app.setScene(Paths.MENU_ADMIN);
        }
    }

    @FXML
    void mostrarMenuVentas(ActionEvent event) {
        System.out.println("Mostrando Menu Ventas");
        App.app.setScene(Paths.MENU_VENTAS);
    }

    @FXML
    void salirSistema(ActionEvent event) {
        String title = "Confirmación de Salida";
        String header = "¿Estás seguro que deseas salir de la aplicación?";
        String context = "Asegúrate de haber guardado todos los cambios recientes.";

        Optional<ButtonType> resultado = Alertas.mostarConfirmation(title, header, context);

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // todo: validar si es necesario llamar al metodo: agendaDAO.guardarCitas(agenda.getCitas());
            System.out.println("Saliendo del sistema");
            Platform.exit();
            System.exit(0);
        }
    }

}
