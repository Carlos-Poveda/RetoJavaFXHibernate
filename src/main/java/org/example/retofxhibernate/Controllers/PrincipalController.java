package org.example.retofxhibernate.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.retofxhibernate.Copia.Copia;
import org.example.retofxhibernate.Copia.CopiaRepository;
import org.example.retofxhibernate.Usuario.Usuario;
import org.example.retofxhibernate.Usuario.UsuarioRepository;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    @FXML private Button btnRefrescar, btnSalir, btnVolver, btnAgregar, btnEliminar;

    private Usuario usuarioLogueado;
    private EntityManagerFactory emf;
    private CopiaRepository copiaRepository;
    private UsuarioRepository usuarioRepository;

    @FXML private TableView<Copia> tablaCopias;
    @FXML private TableColumn<Copia,String> colID, colIDUsuario, colIDPelicula, colEstado, colSoporte;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configuración de columnas
        colID.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getId())));
        colIDPelicula.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getId_pelicula())));
        colIDUsuario.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getId_usuario())));
        colEstado.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getEstado())));
        colSoporte.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getSoporte())));

        // Doble clic para ver detalles
        tablaCopias.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Copia copiaSeleccionada = tablaCopias.getSelectionModel().getSelectedItem();
                if (copiaSeleccionada != null) {
                    mostrarDetallesPelicula(copiaSeleccionada);
                }
            }
        });
    }

    // CAMBIO CLAVE: Setter para JPA/ObjectDB
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        this.copiaRepository = new CopiaRepository(emf);
        this.usuarioRepository = new UsuarioRepository(emf);

        if (this.usuarioLogueado != null) {
            cargarCopiasDelUsuario();
        }
    }

    private void mostrarDetallesPelicula(Copia copia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/detallespeli-view.fxml"));
            Parent root = loader.load();

            DetallesPeliController controller = loader.getController();

            // CAMBIO: Ahora pasamos el emf, no el sessionFactory
            controller.setEntityManagerFactory(this.emf);
            controller.cargarDetalles(copia.getId_pelicula());

            Stage stage = new Stage();
            stage.setTitle("Detalles de Película (ID Copia: " + copia.getId() + ")");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de detalles.");
        }
    }

    @FXML
    public void eliminar(ActionEvent actionEvent) {
        Copia copiaSeleccionada = tablaCopias.getSelectionModel().getSelectedItem();

        if (copiaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona una copia para eliminar.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar Eliminación");
        confirmation.setHeaderText("¿Eliminar copia ID: " + copiaSeleccionada.getId() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Optional<Copia> eliminada = copiaRepository.deleteById(copiaSeleccionada.getId().longValue());

            if (eliminada.isPresent()) {
                cargarCopiasDelUsuario();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Copia eliminada.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar.");
            }
        }
    }

    @FXML
    public void agregar(ActionEvent actionEvent) {
        if (usuarioLogueado.getId() == 1) { // Lógica Admin
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Opciones de Administrador");
            alert.setHeaderText("¿Qué desea agregar?");
            ButtonType btnPeli = new ButtonType("Película");
            ButtonType btnCopia = new ButtonType("Copia");
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnPeli, btnCopia, btnCancelar);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == btnPeli) abrirVentanaPeli();
                else if (result.get() == btnCopia) abrirVentanaCopia();
            }
        } else {
            abrirVentanaCopia();
        }
    }

    private void abrirVentanaPeli() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/agregarpeli-view.fxml"));
            Parent root = loader.load();
            AgregarPeliController ctrl = loader.getController();
            ctrl.setEntityManagerFactory(this.emf);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void abrirVentanaCopia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/agregarcopia-view.fxml"));
            Parent root = loader.load();
            AgregarCopiaController ctrl = loader.getController();
            ctrl.setUsuarioLogueado(usuarioLogueado);
            ctrl.setEntityManagerFactory(this.emf);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refrescar(null); // Refrescamos la tabla al volver
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void cargarCopiasDelUsuario() {
        if (usuarioLogueado == null || copiaRepository == null) return;
        tablaCopias.getItems().clear();
        List<Copia> copias = copiaRepository.findByUserId(usuarioLogueado.getId());
        tablaCopias.getItems().addAll(copias);
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
        if (this.copiaRepository != null) {
            cargarCopiasDelUsuario();
        }
    }

    @FXML
    public void refrescar(ActionEvent actionEvent) {
        cargarCopiasDelUsuario();
    }

    @FXML
    public void volver(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inicio de Sesión");
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) btnVolver.getScene().getWindow()).close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML public void salir(ActionEvent actionEvent) { System.exit(0); }

    private void mostrarAlerta(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}