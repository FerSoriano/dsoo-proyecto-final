package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Venta;
import utils.Alertas;
import utils.Paths;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CorteCajaGeneralController implements Initializable {

    @FXML private Label lblCantidadVentas;
    @FXML private Label lblIngresoTotal;

    @FXML private TableView<Venta> tblHistorial;
    @FXML private TableColumn<Venta, LocalDateTime> colFecha;
    @FXML private TableColumn<Venta, String> colCajero;
    @FXML private TableColumn<Venta, Double> colTotal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colCajero.setCellValueFactory(new PropertyValueFactory<>("cajero"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        colFecha.setCellFactory(columna -> new TableCell<Venta, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime fecha, boolean empty) {
                super.updateItem(fecha, empty);
                if (empty || fecha == null) {
                    setText(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy  hh:mm a");
                    setText(formatter.format(fecha));
                }
            }
        });

        colTotal.setCellFactory(columna -> new TableCell<Venta, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", total));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #2e7d32;"); // Letras verdes
                }
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        tblHistorial.getItems().clear();
        tblHistorial.getItems().addAll(App.app.historialVentas);

        int totalVentas = App.app.historialVentas.size();
        double ingresoTotal = 0.0;

        for (Venta v : App.app.historialVentas) {
            ingresoTotal += v.getTotal();
        }

        lblCantidadVentas.setText(String.valueOf(totalVentas));
        lblIngresoTotal.setText(String.format("$%.2f", ingresoTotal));
    }

    @FXML
    void cerrarDia(ActionEvent event) {
        if (App.app.historialVentas.isEmpty()) {
            Alertas.mostarWarning("Historial Vacío", "No hay ventas para hacer un cierre de día.");
            return;
        }

        String header = "¿Está seguro de realizar el Cierre de Día?";
        String context = "Esto eliminará el historial actual de la pantalla para comenzar un nuevo turno/día en cero. Asegúrese de haber respaldado la información.";

        Optional<ButtonType> resultado = Alertas.mostarConfirmation("Cerrar Día", header, context);

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            App.app.historialVentas.clear();

            cargarDatos();

            Alertas.mostarSuccess("Día Cerrado", "El corte de caja se realizó exitosamente. El sistema está listo para un nuevo día.");
        }
    }

    @FXML
    void regresarMenuAdmin(ActionEvent event) {
        App.app.setScene(Paths.MENU_ADMIN);
    }
}