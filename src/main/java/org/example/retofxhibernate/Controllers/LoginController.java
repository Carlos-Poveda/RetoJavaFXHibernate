package org.example.retofxhibernate.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.retofxhibernate.Common.DataProvider;
import org.example.retofxhibernate.Usuario.Usuario;
import org.example.retofxhibernate.Usuario.UsuarioRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContra;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnSalir;

    private UsuarioRepository usuarioRepository;
    private EntityManagerFactory emf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.emf = DataProvider.getEntityManagerFactory();
        this.usuarioRepository = new UsuarioRepository(emf);
    }

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    private boolean hayConexion() {
        EntityManager em = emf.createEntityManager();
        try {
            // Realiza una consulta válida para verificar la conexión.
            em.createQuery("SELECT u FROM Usuario u").setMaxResults(1).getResultList();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @FXML
    public void inicio(ActionEvent actionEvent) throws IOException {
        if (!hayConexion()) {
            System.out.println("Error de Conexión: verifica el archivo de base de datos.");
            return;
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        Usuario usuarioLogueado = usuarios.stream()
                .filter(u -> u.getNombre_usuario().equals(tfUsuario.getText()) && u.getContraseña().equals(pfContra.getText()))
                .findFirst().orElse(null);

        if (usuarioLogueado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/principal-view.fxml"));
            Parent root = loader.load();

            PrincipalController pc = loader.getController();
            pc.setUsuarioLogueado(usuarioLogueado);
            pc.setEntityManagerFactory(emf); // Pasamos EMF

            Stage stage = (Stage) btnInicio.getScene().getWindow();
            stage.setScene(new Scene(root));
        } else {
            System.out.println("Error: Credenciales incorrectas.");
        }
    }

}