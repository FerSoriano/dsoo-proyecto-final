package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import model.Inventario;
import model.GestorUsuarios;
import model.Sesion;

import model.Venta;
import utils.Paths;

public class App extends Application {
    public static App app;
    private Stage stageWindow;

    public Inventario inventario = new Inventario();
    public GestorUsuarios gestorUsuarios = new GestorUsuarios();
    public ArrayList<Venta> historialVentas = new ArrayList<>();

    public Sesion sesionActual = new Sesion();

    public String moduloDestino;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        app = this;
        stageWindow = stage;

        setScene(Paths.MENU_PRINCIPAL);
    }



    public void setScene(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane pane = loader.load();

//            Object controller = loader.getController();

            stageWindow.setScene(new Scene(pane));
            stageWindow.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
