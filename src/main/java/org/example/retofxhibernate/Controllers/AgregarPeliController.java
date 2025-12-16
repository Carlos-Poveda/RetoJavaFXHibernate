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
import java.util.function.UnaryOperator;

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
        aplicarFiltroNumerico(tfFecha);
        aplicarLimiteLongitud(tfFecha, 4);

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
            alert.setContentText("Por favor, rellene los campos obligatorios.");
            alert.showAndWait();
            return;
        }

        try {
            Pelicula nuevaPelicula = new Pelicula();
            nuevaPelicula.setTitulo(tfTitulo.getText());
            nuevaPelicula.setGenero(tfGenero.getText());
            // Comprobación para que siempre se guarde un año correcto
            if (Integer.parseInt(tfFecha.getText()) < 1800 || Integer.parseInt(tfFecha.getText()) > 2025) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error al guardar la película");
                alert.setHeaderText(null);
                alert.setContentText("El año de lanzamiento no es válido.");
                alert.showAndWait();
                return;
            } else nuevaPelicula.setAño(Integer.valueOf(tfFecha.getText()));
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
            alert.setContentText("Error al guardar la película, la fecha no es válida.");
            alert.showAndWait();
        }
    }

    // Nuevo método para permitir únicamente números en el text field para el año de lanzamiento
    private void aplicarFiltroNumerico(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtro = (TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filtro));
    }
    // Método para permitir solo una cierta cantidad de cifras en el text field del año de lanzamiento
    private void aplicarLimiteLongitud(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }


    private void limpiarCampos() {
        tfTitulo.clear();
        tfGenero.clear();
        tfFecha.clear();
        tfSinopsis.clear();
        tfDirector.clear();
    }
}