package org.example.retofxhibernate.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.retofxhibernate.Copia.Copia;
import org.example.retofxhibernate.Copia.CopiaRepository;
import org.example.retofxhibernate.Usuario.Usuario;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    private Usuario usuarioLogueado;
    private CopiaRepository copiaRepository;
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
        colID.setCellValueFactory( (row)->{
            return new SimpleStringProperty(String.valueOf(row.getValue().getId()));
        });
        colIDPelicula.setCellValueFactory( (row)->{
            return new SimpleStringProperty(String.valueOf(row.getValue().getId_pelicula()));
        });
        colIDUsuario.setCellValueFactory( (row)->{
            return new SimpleStringProperty(String.valueOf(row.getValue().getId_usuario()));
        });
        colEstado.setCellValueFactory( (row)->{
            return new SimpleStringProperty(String.valueOf(row.getValue().getEstado()));
        });
        colSoporte.setCellValueFactory( (row)->{
            return new SimpleStringProperty(String.valueOf(row.getValue().getSoporte()));
        });

        cargarCopiasDelUsuario();


    }

    @javafx.fxml.FXML
    public void eliminar(ActionEvent actionEvent) {

    }

    @javafx.fxml.FXML
    public void agregar(ActionEvent actionEvent) {

    }

    public void cargarCopiasDelUsuario() {
        if (usuarioLogueado == null || copiaRepository == null) {
            System.err.println("Error: Usuario o Repositorio no inicializado.");
            return;
        }

        tablaCopias.getItems().clear();
        List<Copia> copias = copiaRepository.findByUserId(usuarioLogueado.getId());
        tablaCopias.getItems().addAll(copias);
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
        // Una vez que tenemos el usuario y el repositorio (establecido en setCopiaRepository),
        // podemos cargar los datos.
        if (this.copiaRepository != null) {
            cargarCopiasDelUsuario();
        }
    }

    public void setCopiaRepository(SessionFactory sessionFactory) {
        this.copiaRepository = new CopiaRepository(sessionFactory);
        // Intentamos cargar las copias si el usuario ya fue asignado
        if (this.usuarioLogueado != null) {
            cargarCopiasDelUsuario();
        }
    }
}
