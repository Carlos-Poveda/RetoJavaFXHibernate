package org.example.retofxhibernate.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.retofxhibernate.Pelicula.Pelicula;
import org.example.retofxhibernate.Pelicula.PeliculaRepository;

import javax.persistence.EntityManagerFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AgregarPeliController implements Initializable {

    private EntityManagerFactory emf;
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
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAnio())));

        colDescripcion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescripcion()));

        colDirector.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDirector()));
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        this.peliculaRepository = new PeliculaRepository(emf);
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
            alert.setContentText("Por favor, rellene los campos obligatorios.");
            alert.showAndWait();
            return;
        }

        try {
            Pelicula nuevaPelicula = new Pelicula();
            nuevaPelicula.setTitulo(tfTitulo.getText());
            nuevaPelicula.setGenero(tfGenero.getText());
            
            int anio = Integer.parseInt(tfFecha.getText());
            if (anio < 1800 || anio > 2025) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Dato no válido");
                alert.setHeaderText(null);
                alert.setContentText("El año de lanzamiento debe estar entre 1800 y 2025.");
                alert.showAndWait();
                return;
            }
            nuevaPelicula.setAnio(anio);
            
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

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de formato");
            alert.setContentText("El año debe ser un número válido.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al guardar");
            alert.setContentText("Ocurrió un error inesperado al guardar la película: " + e.getMessage());
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