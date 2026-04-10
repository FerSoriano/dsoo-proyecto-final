package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Usuario;
import utils.Alertas;
import utils.Paths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministrarUsuariosController implements Initializable {

    private Usuario usuarioTemp = null;

    // Botones
    @FXML private Button btnAltaUsuario;
    @FXML private Button btnBuscarUsuario;
    @FXML private Button btnEliminarUsuario;
    @FXML private Button btnModificarUsuario;
    @FXML private Button btnRegresar;

    // Tabla y Columnas
    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, String> colUsername;
    @FXML private TableColumn<Usuario, String> colPassword;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colEstatus;

    // Búsqueda
    @FXML private ChoiceBox<String> ddBuscarPor;
    private final String[] opcionesBuscarPor = {"Mostrar todo", "Username"};

    @FXML private Label lblUsernameBuscar;
    @FXML private TextField txtUsernameBuscar;

    // Formulario de Edición
    @FXML private Label lblUsername;
    @FXML private TextField txtUsername;
    @FXML private Label lblPassword;
    @FXML private TextField txtPassword;
    @FXML private Label lblRol;
    @FXML private TextField txtRol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ddBuscarPor.getItems().addAll(opcionesBuscarPor);
        ddBuscarPor.setOnAction(this::mostrarCamposBusqueda);

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("active"));

        colEstatus.setCellFactory(columna -> new TableCell<Usuario, Boolean>() {
            @Override
            protected void updateItem(Boolean isActive, boolean empty) {
                super.updateItem(isActive, empty);
                if (empty || isActive == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (isActive) {
                        setText("Activo");
                        setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;"); // Verde
                    } else {
                        setText("Inactivo");
                        setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;"); // Rojo
                    }
                }
            }
        });

        actualizarTabla(App.app.gestorUsuarios.getListaUsuarios());

        tblUsuarios.setOnMouseClicked(mouseEvent -> {
            if (tblUsuarios.getSelectionModel().getSelectedItem() != null) {
                usuarioTemp = tblUsuarios.getSelectionModel().getSelectedItem();
                mostrarBotonesModificarEliminar();
                mostrarCamposModificarUsuario();
                habilitarCamposModificar();
                cargarDatosModificarUsuario(usuarioTemp);
            }
        });
    }

    private void actualizarTabla(ArrayList<Usuario> usuarios) {
        tblUsuarios.getItems().clear();
        tblUsuarios.getItems().addAll(usuarios);
        tblUsuarios.refresh();
    }


    private void mostrarBotonesModificarEliminar() {
        btnModificarUsuario.setVisible(true);
        btnEliminarUsuario.setVisible(true);
    }

    private void ocultarBotonesModificarEliminar() {
        btnModificarUsuario.setVisible(false);
        btnEliminarUsuario.setVisible(false);
    }

    private void mostrarCamposModificarUsuario() {
        lblUsername.setVisible(true); txtUsername.setVisible(true);
        lblPassword.setVisible(true); txtPassword.setVisible(true);
        lblRol.setVisible(true); txtRol.setVisible(true);
    }

    private void ocultarCamposModificarUsuario() {
        lblUsername.setVisible(false); txtUsername.setVisible(false);
        lblPassword.setVisible(false); txtPassword.setVisible(false);
        lblRol.setVisible(false); txtRol.setVisible(false);
    }

    private void limpiarCamposModificar() {
        txtUsername.clear();
        txtPassword.clear();
        txtRol.clear();
    }

    private void habilitarCamposModificar() {
        txtPassword.setDisable(false);
        txtRol.setDisable(false);
    }

    private void cargarDatosModificarUsuario(Usuario usr) {
        txtUsername.setText(usr.getUsername());
        txtPassword.setText(usr.getPassword());
        txtRol.setText(usr.getRol());
    }

    private void mostrarCamposBusqueda(ActionEvent event) {
        String opcion = ddBuscarPor.getValue();

        lblUsernameBuscar.setVisible(false); txtUsernameBuscar.setVisible(false); txtUsernameBuscar.clear();

        if (opcion != null && opcion.equals("Username")) {
            lblUsernameBuscar.setVisible(true);
            txtUsernameBuscar.setVisible(true);
        }
    }


    @FXML
    void buscarUsuario(ActionEvent event) {
        ArrayList<Usuario> filtro = new ArrayList<>();
        ArrayList<Usuario> todos = App.app.gestorUsuarios.getListaUsuarios();

        if (ddBuscarPor.getValue() == null) {
            Alertas.mostarWarning("Campo Requerido", "Selecciona un método de búsqueda.");
            return;
        }

        if (ddBuscarPor.getValue().equals("Mostrar todo")) {
            filtro = todos;
        } else if (ddBuscarPor.getValue().equals("Username")) {
            String usernameBuscado = txtUsernameBuscar.getText().trim().toLowerCase();
            for (Usuario u : todos) {
                if (u.getUsername().toLowerCase().contains(usernameBuscado)) {
                    filtro.add(u);
                }
            }
        }

        ocultarBotonesModificarEliminar();
        ocultarCamposModificarUsuario();
        actualizarTabla(filtro);
    }

    @FXML
    void modificarUsuario(ActionEvent event) {
        if (!validarCamposNuevos()) return;

        String nuevoRol = txtRol.getText().trim().toUpperCase();

        if (!nuevoRol.equals("ADMIN") && !nuevoRol.equals("VENDEDOR")) {
            Alertas.mostarError("Rol Inválido", "El rol debe ser estrictamente 'ADMIN' o 'VENDEDOR'.");
            return;
        }

        if (usuarioTemp.getRol().equals("ADMIN") && nuevoRol.equals("VENDEDOR")) {
            int adminsActivos = 0;
            for (Usuario u : App.app.gestorUsuarios.getListaUsuarios()) {
                if (u.getRol().equals("ADMIN") && u.isActive()) {
                    adminsActivos++;
                }
            }

            if (adminsActivos <= 1) {
                Alertas.mostarError("Acción Crítica Bloqueada", "No puedes cambiar el rol a VENDEDOR. El sistema debe mantener al menos un Administrador activo en todo momento.");
                return;
            }
        }

        usuarioTemp.setPassword(txtPassword.getText().trim());
        usuarioTemp.setRol(nuevoRol);

        Alertas.mostarSuccess("Operación exitosa", "El usuario se modificó correctamente.");
        actualizarTabla(App.app.gestorUsuarios.getListaUsuarios());
        limpiarCamposModificar();
        ocultarCamposModificarUsuario();
        ocultarBotonesModificarEliminar();
    }

    @FXML
    void eliminarUsuario(ActionEvent event) {

        if (usuarioTemp.getUsername().equals(App.app.sesionActual.getUsuarioActual().getUsername())) {
            Alertas.mostarError("Acción no permitida", "No puedes dar de baja tu propio usuario mientras estás en sesión.");
            return;
        }

        String header = "¿Está seguro que desea dar de baja a este usuario?";
        String context = "Usuario: " + usuarioTemp.getUsername() + "\nSe le revocará el acceso al sistema.";

        Optional<ButtonType> resultado = Alertas.mostarConfirmation("Desactivar Usuario", header, context);

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            usuarioTemp.setActive(false);
            Alertas.mostarSuccess("Operación exitosa", "El usuario ha sido desactivado correctamente.");
            actualizarTabla(App.app.gestorUsuarios.getListaUsuarios());
            limpiarCamposModificar();
            ocultarCamposModificarUsuario();
            ocultarBotonesModificarEliminar();
        }
    }

    private boolean validarCamposNuevos() {
        if (txtPassword.getText().trim().isEmpty() || txtRol.getText().trim().isEmpty()) {
            Alertas.mostarWarning("Campos Vacíos", "La contraseña y el rol no pueden estar vacíos.");
            return false;
        }
        return true;
    }

    @FXML
    void altaUsuario(ActionEvent event) {
        App.app.setScene(Paths.ALTA_USUARIO);
    }

    @FXML
    void regresarMenuPrincipal(ActionEvent event) {
        App.app.setScene(Paths.MENU_ADMIN);
    }
}