package org.example.retofxhibernate.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.retofxhibernate.Pelicula.Pelicula;

import java.net.URL;
import java.util.ResourceBundle;

public class AgregarPeliController implements Initializable {
    @FXML
    private TableView<Pelicula> tablaPeliculas;
    @FXML
    private TableColumn<Pelicula,String> colID;
    @FXML
    private TableColumn<Pelicula,String> colTitulo;
    @FXML
    private TableColumn<Pelicula,String> colGenero;
    @FXML
    private TableColumn<Pelicula,String> colFecha;
    @FXML
    private TableColumn<Pelicula,String> colDescripcion;
    @FXML
    private TableColumn<Pelicula,String> colDirector;
    @FXML
    private Button btnAgregarPeli;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {





    }

    public void agregarPeli(ActionEvent actionEvent) {
    }


}
