package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Producto;
import utils.Alertas;
import utils.Paths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MostrarProductosController implements Initializable {

    private Producto productoTemp = null;

    // Botones
    @FXML private Button btnBuscarProducto;
    @FXML private Button btnCrearProducto;
    @FXML private Button btnEliminarProducto;
    @FXML private Button btnModificarProducto;
    @FXML private Button btnRegresar;

    // Tabla y Columnas
    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, Integer> colID;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecioCompra;
    @FXML private TableColumn<Producto, Double> colPrecioVenta;
    @FXML private TableColumn<Producto, Integer> colExistencias;
    @FXML private TableColumn<Producto, Integer> colNivelReorden;
    @FXML private TableColumn<Producto, Boolean> colNecesitaReorden;

    // Búsqueda
    @FXML private ChoiceBox<String> ddBuscarPor;
    private final String[] opcionesBuscarPor = {"Mostrar todo", "ID", "Nombre"};

    @FXML private Label lblIDBuscar;
    @FXML private TextField txtIDBuscar;
    @FXML private Label lblNombreBuscar;
    @FXML private TextField txtNombreBuscar;

    // Formulario de Edición
    @FXML private Label lblID;
    @FXML private TextField txtID;
    @FXML private Label lblNombre;
    @FXML private TextField txtNombre;
    @FXML private Label lblPrecioCompra;
    @FXML private TextField txtPrecioCompra;
    @FXML private Label lblPrecioVenta;
    @FXML private TextField txtPrecioVenta;
    @FXML private Label lblExistencias;
    @FXML private TextField txtExistencias;
    @FXML private Label lblNivelReorden;
    @FXML private TextField txtNivelReorden;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ddBuscarPor.getItems().addAll(opcionesBuscarPor);
        ddBuscarPor.setOnAction(this::mostrarCamposBusqueda);

        colID.setCellValueFactory(new PropertyValueFactory<>("productoID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colExistencias.setCellValueFactory(new PropertyValueFactory<>("existencias"));
        colNivelReorden.setCellValueFactory(new PropertyValueFactory<>("nivelReorden"));
        colNecesitaReorden.setCellValueFactory(new PropertyValueFactory<>("necesitaReorden"));

        // 1. CELL FACTORY: Cambiar el texto de true/false a "Resurtir" / "OK"
        colNecesitaReorden.setCellFactory(columna -> new TableCell<Producto, Boolean>() {
            @Override
            protected void updateItem(Boolean necesitaReorden, boolean empty) {
                super.updateItem(necesitaReorden, empty);

                if (empty || necesitaReorden == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (necesitaReorden) {
                        setText("Resurtir");
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #b30000;"); // Texto rojo oscuro
                    } else {
                        setText("OK");
                        setStyle("-fx-text-fill: #388e3c;"); // Texto verde
                    }
                }
            }
        });

        // 2. ROW FACTORY: Pintar toda la fila de rojo claro si necesita reorden
        tblProductos.setRowFactory(tv -> new TableRow<Producto>() {
            @Override
            protected void updateItem(Producto producto, boolean empty) {
                super.updateItem(producto, empty);

                if (producto == null || empty) {
                    setStyle(""); // Limpia el estilo de las filas vacías
                } else {
                    if (producto.isNecesitaReorden()) {
                        // Aplica un color de fondo rojo claro (#ffcccc) a la fila entera
                        setStyle("-fx-control-inner-background: #ffcccc; -fx-control-inner-background-alt: #ffcccc;");
                    } else {
                        // Si está bien, le quitamos cualquier estilo manual para que tome el color por defecto
                        setStyle("");
                    }
                }
            }
        });

        actualizarTabla(App.app.inventario.getListaProductosActivos());

        tblProductos.setOnMouseClicked(mouseEvent -> {
            if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                productoTemp = tblProductos.getSelectionModel().getSelectedItem();
                mostrarBotonesModificarEliminar();
                mostrarCamposModificarProducto();
                habilitarCamposModificar();
                cargarDatosModificarProducto(productoTemp);
            }
        });
    }

    private void actualizarTabla(ArrayList<Producto> productos) {
        tblProductos.getItems().clear();
        tblProductos.getItems().addAll(productos);
        tblProductos.refresh();
    }

    private void mostrarBotonesModificarEliminar() {
        btnModificarProducto.setVisible(true);
        btnEliminarProducto.setVisible(true);
    }

    private void ocultarBotonesModificarEliminar() {
        btnModificarProducto.setVisible(false);
        btnEliminarProducto.setVisible(false);
    }

    private void mostrarCamposModificarProducto() {
        lblID.setVisible(true); txtID.setVisible(true);
        lblNombre.setVisible(true); txtNombre.setVisible(true);
        lblPrecioCompra.setVisible(true); txtPrecioCompra.setVisible(true);
        lblPrecioVenta.setVisible(true); txtPrecioVenta.setVisible(true);
        lblExistencias.setVisible(true); txtExistencias.setVisible(true);
        lblNivelReorden.setVisible(true); txtNivelReorden.setVisible(true);
    }

    private void ocultarCamposModificarProducto() {
        lblID.setVisible(false); txtID.setVisible(false);
        lblNombre.setVisible(false); txtNombre.setVisible(false);
        lblPrecioCompra.setVisible(false); txtPrecioCompra.setVisible(false);
        lblPrecioVenta.setVisible(false); txtPrecioVenta.setVisible(false);
        lblExistencias.setVisible(false); txtExistencias.setVisible(false);
        lblNivelReorden.setVisible(false); txtNivelReorden.setVisible(false);
    }

    private void limpiarCamposModificar() {
        txtID.clear(); txtNombre.clear();
        txtPrecioCompra.clear(); txtPrecioVenta.clear();
        txtExistencias.clear(); txtNivelReorden.clear();
    }

    private void deshabilitarCamposModificar() {
        txtNombre.setDisable(true);
        txtPrecioCompra.setDisable(true);
        txtPrecioVenta.setDisable(true);
        txtExistencias.setDisable(true);
        txtNivelReorden.setDisable(true);
    }

    private void habilitarCamposModificar() {
        txtNombre.setDisable(false);
        txtPrecioCompra.setDisable(false);
        txtPrecioVenta.setDisable(false);
        txtExistencias.setDisable(false);
        txtNivelReorden.setDisable(false);
    }

    private void cargarDatosModificarProducto(Producto prod) {
        txtID.setText(String.valueOf(prod.getProductoID()));
        txtNombre.setText(prod.getNombre());
        txtPrecioCompra.setText(String.valueOf(prod.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(prod.getPrecioVenta()));
        txtExistencias.setText(String.valueOf(prod.getExistencias()));
        txtNivelReorden.setText(String.valueOf(prod.getNivelReorden()));
    }

    private void mostrarCamposBusqueda(ActionEvent event) {
        String opcion = ddBuscarPor.getValue();

        lblIDBuscar.setVisible(false); txtIDBuscar.setVisible(false); txtIDBuscar.clear();
        lblNombreBuscar.setVisible(false); txtNombreBuscar.setVisible(false); txtNombreBuscar.clear();

        if (opcion != null) {
            switch (opcion) {
                case "ID" -> {
                    lblIDBuscar.setVisible(true);
                    txtIDBuscar.setVisible(true);
                }
                case "Nombre" -> {
                    lblNombreBuscar.setVisible(true);
                    txtNombreBuscar.setVisible(true);
                }
            }
        }
    }


    @FXML
    void buscarProducto(ActionEvent event) {
        ArrayList<Producto> filtro = new ArrayList<>();
        ArrayList<Producto> todos = App.app.inventario.getListaProductosActivos();

        if (ddBuscarPor.getValue() == null) {
            Alertas.mostarWarning("Campo Requerido", "Selecciona un método de búsqueda.");
            return;
        }

        if (ddBuscarPor.getValue().equals("Mostrar todo")) {
            filtro = todos;
        }
        else if (ddBuscarPor.getValue().equals("Nombre")) {
            String nombreBuscado = txtNombreBuscar.getText().trim().toLowerCase();
            for (Producto p : todos) {
                if (p.getNombre().toLowerCase().contains(nombreBuscado)) {
                    filtro.add(p);
                }
            }
        }
        else if (ddBuscarPor.getValue().equals("ID")) {
            try {
                int idBuscado = Integer.parseInt(txtIDBuscar.getText().trim());
                for (Producto p : todos) {
                    if (p.getProductoID() == idBuscado) {
                        filtro.add(p);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                Alertas.mostarError("Formato inválido", "El ID debe ser un número entero.");
                return;
            }
        }

        ocultarBotonesModificarEliminar();
        ocultarCamposModificarProducto();
        actualizarTabla(filtro);
    }

    @FXML
    void modificarProducto(ActionEvent event) {
        if (!validarCamposNuevos()) return;

        try {
            productoTemp.setNombre(txtNombre.getText().trim());
            productoTemp.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText().trim()));
            productoTemp.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText().trim()));
            productoTemp.setExistencias(Integer.parseInt(txtExistencias.getText().trim()));
            productoTemp.setNivelReorden(Integer.parseInt(txtNivelReorden.getText().trim()));

            Alertas.mostarSuccess("Operación exitosa", "El producto se modificó correctamente.");
            actualizarTabla(App.app.inventario.getListaProductosActivos());

        } catch (NumberFormatException e) {
            Alertas.mostarError("Error de formato", "Revisa que los precios sean decimales y las existencias números enteros.");
        }
    }

    @FXML
    void eliminarProducto(ActionEvent event) {
        String header = "¿Está seguro que desea dar de baja este producto?";
        String context = "Producto: " + productoTemp.getNombre() + "\nEl producto cambiará su estatus a inactivo en el sistema.";

        Optional<ButtonType> resultado = Alertas.mostarConfirmation("Desactivar Producto", header, context);

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            productoTemp.setActive(false);

            Alertas.mostarSuccess("Operación exitosa", "El producto ha sido desactivado correctamente.");

            actualizarTabla(App.app.inventario.getListaProductosActivos());

            limpiarCamposModificar();
            ocultarCamposModificarProducto();
            ocultarBotonesModificarEliminar();
        }
    }

    private boolean validarCamposNuevos() {
        if (txtNombre.getText().trim().isEmpty() || txtPrecioCompra.getText().trim().isEmpty() ||
                txtPrecioVenta.getText().trim().isEmpty() || txtExistencias.getText().trim().isEmpty()) {
            Alertas.mostarWarning("Campos Vacíos", "Ningún campo importante puede estar vacío.");
            return false;
        }
        return true;
    }

    @FXML
    void irACrearProducto(ActionEvent event) {
        // App.app.setScene(Paths.CREAR_PRODUCTO);
    }

    @FXML
    void regresarMenuPrincipal(ActionEvent event) {
        App.app.setScene(Paths.MENU_ADMIN);
    }
}