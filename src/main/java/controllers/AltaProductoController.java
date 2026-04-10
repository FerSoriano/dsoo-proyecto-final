package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Producto;
import utils.Alertas;
import utils.Paths;

public class AltaProductoController {

    @FXML private Button btnCrearProducto;
    @FXML private Button btnRegresar;
    @FXML private Label lblExistencias;
    @FXML private Label lblNivelReorden;
    @FXML private Label lblNombreProducto;
    @FXML private Label lblPrecioCompra;
    @FXML private Label lblPrecioVenta;
    @FXML private TextField txtExistencias;
    @FXML private TextField txtNivelReorden;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtPrecioCompra;
    @FXML private TextField txtPrecioVenta;

    @FXML
    void crearProducto(ActionEvent event) {
        if (!validarCamposNuevos()) return;

        try {
            String nombre = txtNombreProducto.getText().trim();
            double precioCompra = Double.parseDouble(txtPrecioCompra.getText().trim());
            double precioVenta = Double.parseDouble(txtPrecioVenta.getText().trim());
            int existencias = Integer.parseInt(txtExistencias.getText().trim());
            int nivelReorden = Integer.parseInt(txtNivelReorden.getText().trim());

            Producto productoTemp = App.app.inventario.obtenerProductoPorNombre(nombre);

            if (productoTemp == null) {
                productoTemp = new Producto(nombre, precioCompra, precioVenta, existencias, nivelReorden);
                App.app.inventario.agregarProducto(productoTemp);
                Alertas.mostarSuccess("Éxito", "El producto se ha dado de alta correctamente.");
                limpiarCampos();

            } else {
                if (!productoTemp.isActive()) {
                    productoTemp.setPrecioCompra(precioCompra);
                    productoTemp.setPrecioVenta(precioVenta);
                    productoTemp.setExistencias(existencias);
                    productoTemp.setNivelReorden(nivelReorden);
                    productoTemp.setActive(true);

                    Alertas.mostarSuccess("Producto Reactivado", "El producto '" + productoTemp.getNombre() + "' estaba inactivo y ha sido reactivado con los nuevos datos.");
                    limpiarCampos();
                } else {
                    Alertas.mostarWarning("Producto Registrado", "Este producto ya está dado de alta y se encuentra activo.");
                }
            }

        } catch (NumberFormatException e) {
            Alertas.mostarError("Error de formato", "Revisa que los precios sean números decimales y las existencias/reorden sean números enteros.");
        }
    }

    private boolean validarCamposNuevos() {
        if (txtNombreProducto.getText().trim().isEmpty() ||
                txtPrecioCompra.getText().trim().isEmpty() ||
                txtPrecioVenta.getText().trim().isEmpty() ||
                txtExistencias.getText().trim().isEmpty() ||
                txtNivelReorden.getText().trim().isEmpty()) {

            Alertas.mostarWarning("Campos Vacíos", "Ningún campo importante puede estar vacío.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtNombreProducto.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        txtExistencias.clear();
        txtNivelReorden.clear();
    }

    @FXML
    void regresarMenuAdmin(ActionEvent event) {
        App.app.setScene(Paths.ADMINISTRAR_PRODUCTOS);
    }
}