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
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnVolver;

    private Usuario usuarioLogueado;
    private SessionFactory sessionFactory;
    private CopiaRepository copiaRepository;
    private UsuarioRepository usuarioRepository;

    @javafx.fxml.FXML
    private TableView<Copia> tablaCopias;
    @javafx.fxml.FXML
    private TableColumn<Copia,String> colID;
    @javafx.fxml.FXML
    private TableColumn<Copia,String> colIDUsuario;
    @javafx.fxml.FXML
    private TableColumn<Copia,String> colIDPelicula;
    @javafx.fxml.FXML
    private TableColumn<Copia,String> colEstado;
    @javafx.fxml.FXML
    private TableColumn<Copia,String> colSoporte;

    @javafx.fxml.FXML
    private Button btnAgregar;
    @javafx.fxml.FXML
    private Button btnEliminar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getId())));
        colIDPelicula.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getId_pelicula())));
        colIDUsuario.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getId_usuario())));
        colEstado.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getEstado())));
        colSoporte.setCellValueFactory( (row)-> new SimpleStringProperty(String.valueOf(row.getValue().getSoporte())));
        tablaCopias.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detectar doble clic
                Copia copiaSeleccionada = tablaCopias.getSelectionModel().getSelectedItem();
                if (copiaSeleccionada != null) {
                    mostrarDetallesPelicula(copiaSeleccionada); // Llamar a la nueva función
                }
            }
        });
    }

    private void mostrarDetallesPelicula(Copia copia) {
        Integer idPelicula = copia.getId_pelicula();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/detallespeli-view.fxml"));
            Parent root = loader.load();

            DetallesPeliController controller = loader.getController();

            controller.setSessionFactory(this.sessionFactory);

            controller.cargarDetalles(idPelicula);

            Stage stage = new Stage();
            stage.setTitle("Detalles de Película (ID Copia: " + copia.getId() + ")");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al abrir la ventana de detalles: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void eliminar(ActionEvent actionEvent) {
        Copia copiaSeleccionada = tablaCopias.getSelectionModel().getSelectedItem();

        if (copiaSeleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "Por favor, selecciona la copia que deseas eliminar de la tabla."
            );
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar Eliminación");
        confirmation.setHeaderText("¿Estás seguro de que quieres eliminar esta copia?");
        confirmation.setContentText("Copia ID: " + copiaSeleccionada.getId() + " (Película ID: " + copiaSeleccionada.getId_pelicula() + ")");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Long idCopia = copiaSeleccionada.getId().longValue();
            Optional<Copia> copiaEliminada = copiaRepository.deleteById(idCopia);

            if (copiaEliminada.isPresent()) {
                cargarCopiasDelUsuario();
                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Eliminación Exitosa",
                        "La copia ha sido eliminada de tu lista."
                );
            } else {
                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        "No se pudo eliminar la copia. Inténtalo de nuevo."
                );
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Modificación del método agregar para añadir la opción al administrador de agregar copias
    @FXML
    public void agregar(ActionEvent actionEvent) {
        if (usuarioLogueado.getId() == 1) {
            Alert selector = new Alert(Alert.AlertType.CONFIRMATION);
            selector.setTitle("Menú de administrador");
            selector.setHeaderText(null);
            selector.setContentText("¿Qué desea agregar?");
            ButtonType btnPelicula = new ButtonType("Nueva película");
            ButtonType btnCopia = new ButtonType("Nueva copia");
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            selector.getButtonTypes().setAll(btnPelicula, btnCopia, btnCancelar);
            Optional<ButtonType> resultado = selector.showAndWait();
            if (resultado.isPresent()) {
                if (resultado.get() == btnPelicula) {
                    abrirVentanaAgregarPelicula();
                } else if (resultado.get() == btnCopia) {
                    abrirVentanaAgregarCopia();
                }
            }
        } else {
            abrirVentanaAgregarCopia();
        }
    }

    // Método auxiliar para abrir la ventana de Películas
    private void abrirVentanaAgregarPelicula() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/agregarpeli-view.fxml"));
            Parent root = loader.load();
            AgregarPeliController controller = loader.getController();
            controller.setSessionFactory(this.sessionFactory);
            Stage stage = new Stage();
            stage.setTitle("Añadir Nueva Película");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de agregar película.");
        }
    }

    // Método auxiliar para abrir la ventana de Copias
    private void abrirVentanaAgregarCopia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/agregarcopia-view.fxml"));
            Parent root = loader.load();
            AgregarCopiaController controller = loader.getController();
            controller.setSessionFactory(this.sessionFactory);
            controller.setUsuarioLogueado(this.usuarioLogueado);
            Stage stage = new Stage();
            stage.setTitle("Añadir Nueva Copia");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refrescar(null);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de agregar copia.");
        }
    }

    public void cargarCopiasDelUsuario() {
        if (usuarioLogueado == null || copiaRepository == null) {
            return;
        }
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

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.copiaRepository = new CopiaRepository(sessionFactory);
        this.usuarioRepository = new UsuarioRepository(sessionFactory);

        if (this.usuarioLogueado != null) {
            cargarCopiasDelUsuario();
        }
    }

    @FXML
    public void volver(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/login-view.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Inicio de Sesión");
            loginStage.setScene(new Scene(root));

            loginStage.show();

            Stage currentStage = (Stage) btnVolver.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista de login.");
        }
    }

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void refrescar(ActionEvent actionEvent) {
        tablaCopias.getItems().clear();
        List<Copia> copias = copiaRepository.findByUserId(usuarioLogueado.getId());
        tablaCopias.getItems().addAll(copias);
    }
}