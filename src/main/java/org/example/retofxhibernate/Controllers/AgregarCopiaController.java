package org.example.retofxhibernate.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*; // Importar Alert
import org.example.retofxhibernate.Copia.Copia;
import org.example.retofxhibernate.Copia.CopiaRepository;
import org.example.retofxhibernate.Pelicula.Pelicula;
import org.example.retofxhibernate.Pelicula.PeliculaRepository;
import org.example.retofxhibernate.Usuario.Usuario; // Importar Usuario
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AgregarCopiaController implements Initializable {

    // --- Campos de UI ---
    @FXML
    private TableView<Pelicula> tablaPelis;
    @FXML
    private TableColumn<Pelicula, String> colPeliID;
    @FXML
    private TableColumn<Pelicula, String> colPeliTitulo;
    @FXML
    private TableColumn<Pelicula, String> colPeliGenero;
    @FXML
    private TableColumn<Pelicula, String> colPeliFecha;
    @FXML
    private TableColumn<Pelicula, String> colPeliDescrip;
    @FXML
    private TableColumn<Pelicula, String> colPeliDirector;

    @FXML
    private TextField tfIdPeli;
    @FXML
    private TextField tfEstado;
    @FXML
    private TextField tfSoporte;

    @FXML
    private Button btnAgregar;

    private SessionFactory sessionFactory;
    private CopiaRepository copiaRepository;
    private PeliculaRepository peliculaRepository;
    private Usuario usuarioLogueado;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPeliID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        colPeliTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        colPeliGenero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenero()));
        colPeliFecha.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAño())));
        colPeliDescrip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colPeliDirector.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirector()));

        // Configuración de selección: Llena el campo tfIdPeli al seleccionar una fila
        tablaPelis.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tfIdPeli.setText(String.valueOf(newSelection.getId()));
            }
        });
    }

    @FXML
    public void agregar(ActionEvent actionEvent) {
        String idPeliText = tfIdPeli.getText();
        String estado = tfEstado.getText();
        String soporte = tfSoporte.getText();

        if (idPeliText.isEmpty() || estado.isEmpty() || soporte.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error", "Debe rellenar todos los campos (ID Película, Estado y Soporte).");
            return;
        }

        Integer idPelicula;
        try {
            idPelicula = Integer.parseInt(idPeliText);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "El ID de la Película debe ser un número.");
            return;
        }

        // Comprobación del ID para ver si existe
        var peliculaExiste = peliculaRepository.findById(Long.valueOf(idPelicula));

        if (peliculaExiste.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Película no encontrada","No existe ninguna película con el ID " + idPelicula + " en la base de datos.");
            return;
        }

        // Si llegamos aquí, la película existe. Procedemos a guardar.
        Copia nuevaCopia = new Copia();
        nuevaCopia.setId_pelicula(idPelicula);
        nuevaCopia.setId_usuario(usuarioLogueado.getId());
        nuevaCopia.setEstado(estado);
        nuevaCopia.setSoporte(soporte);

        Copia guardada = copiaRepository.save(nuevaCopia);

        if (guardada != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Copia añadida correctamente a tu lista.");
            tfIdPeli.clear();
            tfEstado.clear();
            tfSoporte.clear();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la copia en la base de datos.");
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.copiaRepository = new CopiaRepository(sessionFactory);
        this.peliculaRepository = new PeliculaRepository(sessionFactory); // Inicializamos el repositorio de películas

        if (this.usuarioLogueado != null) {
            cargarPeliculas();
        }
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
        if (this.sessionFactory != null) {
            cargarPeliculas();
        }
    }


    private void cargarPeliculas() {
        if (peliculaRepository != null) {
            tablaPelis.getItems().clear();
            List<Pelicula> peliculas = peliculaRepository.findAll();
            tablaPelis.getItems().addAll(peliculas);
        }
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}