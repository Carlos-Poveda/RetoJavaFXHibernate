package org.example.retofxhibernate.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.retofxhibernate.Pelicula.Pelicula;
import org.example.retofxhibernate.Pelicula.PeliculaRepository;

// CAMBIO: Importar JPA en lugar de Hibernate
import javax.persistence.EntityManagerFactory;

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

    // CAMBIO: Usamos la factoría de JPA
    private EntityManagerFactory emf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Normalmente se deja vacío si la carga depende de datos externos
    }

    // CAMBIO: El método ahora recibe el EntityManagerFactory
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void cargarDetalles(Integer idPelicula) {
        if (emf == null) {
            System.err.println("Error: EntityManagerFactory no inicializada.");
            return;
        }

        // El repositorio ahora se inicializa con el emf de JPA
        PeliculaRepository peliculaRepository = new PeliculaRepository(emf);

        // Buscamos la película en el archivo .odb
        Optional<Pelicula> peliculaOpt = peliculaRepository.findById(idPelicula.longValue());

        if (peliculaOpt.isPresent()) {
            Pelicula peli = peliculaOpt.get();
            lblTitulo.setText(peli.getTitulo());
            lblGenero.setText(peli.getGenero());
            lblDirector.setText(peli.getDirector());

            if (peli.getAnio() != null) {
                lblAnio.setText(String.valueOf(peli.getAnio()));
            } else {
                lblAnio.setText("Sin datos");
            }

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