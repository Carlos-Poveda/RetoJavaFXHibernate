package org.example.retofxhibernate.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.retofxhibernate.Pelicula.Pelicula;
import org.example.retofxhibernate.Pelicula.PeliculaRepository;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AgregarPeliController implements Initializable {

    private SessionFactory sessionFactory;
    private PeliculaRepository peliculaRepository;

    @FXML
    private TextField tfTitulo;
    @FXML
    private TextField tfGenero;
    @FXML
    private TextField tfFecha;
    @FXML
    private TextField tfSinopsis;
    @FXML
    private TextField tfDirector;

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
        colID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        colTitulo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitulo()));

        colGenero.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGenero()));

        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAño())));

        colDescripcion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescripcion()));

        colDirector.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDirector()));
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.peliculaRepository = new PeliculaRepository(sessionFactory);
        cargarPeliculas();
    }

    private void cargarPeliculas() {
        if (peliculaRepository != null) {
            tablaPeliculas.getItems().clear();
            List<Pelicula> peliculas = peliculaRepository.findAll();
            tablaPeliculas.getItems().addAll(peliculas);
        }
    }

    @FXML
    public void agregarPeli(ActionEvent actionEvent) {
        if (tfTitulo.getText().isEmpty() || tfDirector.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setContentText("Por favor, rellena al menos Título y Director.");
            alert.showAndWait();
            return;
        }

        try {
            Pelicula nuevaPelicula = new Pelicula();
            nuevaPelicula.setTitulo(tfTitulo.getText());
            nuevaPelicula.setGenero(tfGenero.getText());
            nuevaPelicula.setAño(Integer.valueOf(tfFecha.getText()));
            nuevaPelicula.setDescripcion(tfSinopsis.getText());
            nuevaPelicula.setDirector(tfDirector.getText());

            peliculaRepository.save(nuevaPelicula);

            cargarPeliculas();
            limpiarCampos();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Película guardada correctamente.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error al guardar la película: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void limpiarCampos() {
        tfTitulo.clear();
        tfGenero.clear();
        tfFecha.clear();
        tfSinopsis.clear();
        tfDirector.clear();
    }
}