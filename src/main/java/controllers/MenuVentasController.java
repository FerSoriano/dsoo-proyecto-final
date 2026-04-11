package controllers;

import application.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.DetalleVenta;
import model.Producto;
import model.Venta;
import utils.Alertas;
import utils.Paths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuVentasController implements Initializable {

    @FXML private Label lblCajero;
    @FXML private Label lblTotal;
    @FXML private TextField txtBuscar;

    // Tabla Catálogo (Izquierda)
    @FXML private TableView<Producto> tblCatalogo;
    @FXML private TableColumn<Producto, String> colCatNombre;
    @FXML private TableColumn<Producto, Double> colCatPrecio;
    @FXML private TableColumn<Producto, Integer> colCatStock;

    // Tabla Carrito (Derecha)
    @FXML private TableView<DetalleVenta> tblCarrito;
    @FXML private TableColumn<DetalleVenta, String> colCarNombre;
    @FXML private TableColumn<DetalleVenta, Integer> colCarCant;
    @FXML private TableColumn<DetalleVenta, Double> colCarSubtotal;

    // Lista observable para que la tabla del carrito se actualice sola
    private ObservableList<DetalleVenta> listaCarrito = FXCollections.observableArrayList();
    private double totalVenta = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (App.app.sesionActual.isLogged()) {
            lblCajero.setText("Cajero: " + App.app.sesionActual.getUsuarioActual().getUsername());
        }

        colCatNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCatPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colCatStock.setCellValueFactory(new PropertyValueFactory<>("existencias"));

        colCarNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCarCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCarSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tblCarrito.setItems(listaCarrito);
        cargarCatalogo(App.app.inventario.getListaProductosActivos());
    }

    private void cargarCatalogo(ArrayList<Producto> productos) {
        tblCatalogo.getItems().clear();
        tblCatalogo.getItems().addAll(productos);
    }

    @FXML
    void filtrarProductos(KeyEvent event) {
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        ArrayList<Producto> filtro = new ArrayList<>();

        for (Producto p : App.app.inventario.getListaProductosActivos()) {
            if (p.getNombre().toLowerCase().contains(busqueda)) {
                filtro.add(p);
            }
        }
        cargarCatalogo(filtro);
    }

    @FXML
    void agregarAlCarrito(ActionEvent event) {
        Producto productoSeleccionado = tblCatalogo.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            Alertas.mostarWarning("Selección Requerida", "Selecciona un producto del catálogo primero.");
            return;
        }

        int cantidadEnCarrito = 0;
        DetalleVenta detalleExistente = null;

        for (DetalleVenta d : listaCarrito) {
            if (d.getProducto().getProductoID() == productoSeleccionado.getProductoID()) {
                cantidadEnCarrito = d.getCantidad();
                detalleExistente = d;
                break;
            }
        }

        if (cantidadEnCarrito + 1 > productoSeleccionado.getExistencias()) {
            Alertas.mostarError("Sin Stock", "No hay suficientes existencias de este producto.");
            return;
        }

        if (detalleExistente != null) {
            detalleExistente.setCantidad(detalleExistente.getCantidad() + 1);
            tblCarrito.refresh();
        } else {
            listaCarrito.add(new DetalleVenta(productoSeleccionado, 1));
        }

        calcularTotal();
    }

    @FXML
    void quitarDelCarrito(ActionEvent event) {
        DetalleVenta detalleSeleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (detalleSeleccionado != null) {
            listaCarrito.remove(detalleSeleccionado);
            calcularTotal();
        }
    }

    @FXML
    void vaciarCarrito(ActionEvent event) {
        listaCarrito.clear();
        calcularTotal();
    }

    private void calcularTotal() {
        totalVenta = 0.0;
        for (DetalleVenta d : listaCarrito) {
            totalVenta += d.getSubtotal();
        }
        lblTotal.setText(String.format("$%.2f", totalVenta));
    }

    @FXML
    void cobrar(ActionEvent event) {
        if (listaCarrito.isEmpty()) {
            Alertas.mostarWarning("Carrito Vacío", "Agrega productos al carrito antes de cobrar.");
            return;
        }

        Optional<ButtonType> confirmacion = Alertas.mostarConfirmation("Cobrar", "¿Finalizar Venta?", "El total es: $" + String.format("%.2f", totalVenta));

        if (confirmacion.isPresent() && confirmacion.get() == ButtonType.OK) {

            for (DetalleVenta d : listaCarrito) {
                Producto p = d.getProducto();
                p.setExistencias(p.getExistencias() - d.getCantidad());
            }

            String cajeroActual = App.app.sesionActual.getUsuarioActual().getUsername();
            Venta nuevaVenta = new Venta(cajeroActual, totalVenta);
            App.app.historialVentas.add(nuevaVenta);

            mostrarTicket();

            listaCarrito.clear();
            calcularTotal();
            cargarCatalogo(App.app.inventario.getListaProductosActivos()); // Refrescamos tabla izq para ver el nuevo stock
        }
    }

    private void mostrarTicket() {
        StringBuilder ticket = new StringBuilder();
        ticket.append("==============================\n");
        ticket.append("        TICKET DE VENTA       \n");
        ticket.append("==============================\n");
        ticket.append("Cajero: ").append(App.app.sesionActual.getUsuarioActual().getUsername()).append("\n");
        ticket.append("------------------------------\n");
        ticket.append(String.format("%-15s %-5s %-10s\n", "Producto", "Cant", "Subtotal"));

        for (DetalleVenta d : listaCarrito) {
            ticket.append(String.format("%-15s %-5d $%-9.2f\n",
                    d.getNombreProducto().length() > 14 ? d.getNombreProducto().substring(0, 14) : d.getNombreProducto(),
                    d.getCantidad(),
                    d.getSubtotal()));
        }

        ticket.append("------------------------------\n");
        ticket.append(String.format("TOTAL:                 $%.2f\n", totalVenta));
        ticket.append("==============================\n");
        ticket.append("    ¡Gracias por tu compra!   \n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venta Exitosa");
        alert.setHeaderText("Ticket generado");

        // Usamos un TextArea para que la fuente monoespaciada (tipo impresora de tickets) se respete
        TextArea textArea = new TextArea(ticket.toString());
        textArea.setEditable(false);
        textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;");
        alert.getDialogPane().setContent(textArea);

        alert.showAndWait();
    }

    @FXML
    void restarDelCarrito(ActionEvent event) {
        DetalleVenta detalleSeleccionado = tblCarrito.getSelectionModel().getSelectedItem();

        if (detalleSeleccionado == null) {
            Alertas.mostarWarning("Selección Requerida", "Selecciona un producto del carrito para restarle cantidad.");
            return;
        }

        if (detalleSeleccionado.getCantidad() > 1) {
            detalleSeleccionado.setCantidad(detalleSeleccionado.getCantidad() - 1);
            tblCarrito.refresh();
        } else {
            listaCarrito.remove(detalleSeleccionado);
        }

        calcularTotal();
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        String cajeroActual = App.app.sesionActual.getUsuarioActual().getUsername();
        int totalVentasRealizadas = 0;
        double dineroRecaudado = 0.0;

        for (Venta v : App.app.historialVentas) {
            if (v.getCajero().equals(cajeroActual)) {
                totalVentasRealizadas++;
                dineroRecaudado += v.getTotal();
            }
        }

        String header = "Corte de Caja - Turno Finalizado";
        String context = "Cajero: " + cajeroActual + "\n" +
                "---------------------------------\n" +
                "Ventas realizadas: " + totalVentasRealizadas + "\n" +
                "Total en caja: $" + String.format("%.2f", dineroRecaudado) + "\n" +
                "---------------------------------\n" +
                "¿Confirmar entrega de caja y salir?";

        Optional<ButtonType> confirmacion = Alertas.mostarConfirmation("Cerrar Sesión", header, context);

        if (confirmacion.isPresent() && confirmacion.get() == ButtonType.OK) {
            App.app.sesionActual.cerrarSesion();
            App.app.setScene(Paths.MENU_PRINCIPAL);
        }
    }
}