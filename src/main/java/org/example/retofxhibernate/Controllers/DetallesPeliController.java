package org.example.retofxhibernate.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.retofxhibernate.Pelicula.Pelicula;
import org.example.retofxhibernate.Pelicula.PeliculaRepository;
import org.hibernate.SessionFactory;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DetallesPeliController implements Initializable {
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblDirector;
    @FXML
    private Label lblAnio;
    @FXML
    private Label lblSinopsis;

    private SessionFactory sessionFactory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void cargarDetalles(Integer idPelicula) {
        if (sessionFactory == null) {
            System.err.println("Error: SessionFactory no inicializada.");
            return;
        }

        PeliculaRepository peliculaRepository = new PeliculaRepository(sessionFactory);

        Optional<Pelicula> peliculaOpt = peliculaRepository.findById(idPelicula.longValue());

        if (peliculaOpt.isPresent()) {
            Pelicula peli = peliculaOpt.get();
            lblTitulo.setText(peli.getTitulo());
            lblGenero.setText(peli.getGenero());
            lblDirector.setText(peli.getDirector());
            if (peli.getAño()!= null) {
                lblAnio.setText(peli.getAño().toString());
            } else lblAnio.setText("sin datos");
            lblSinopsis.setText(peli.getDescripcion());
        } else {
            lblTitulo.setText("PELÍCULA NO ENCONTRADA");
            lblSinopsis.setText("No se encontraron detalles para el ID: " + idPelicula);
        }
    }

    @FXML
    public void volver(ActionEvent actionEvent) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
}