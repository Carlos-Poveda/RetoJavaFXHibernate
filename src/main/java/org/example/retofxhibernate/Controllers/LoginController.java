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
import org.hibernate.Session; // Importar Session

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Obtenemos la factoría, pero NO intentamos conectar hasta que el usuario pulse el botón
        usuarioRepository = new UsuarioRepository(DataProvider.getSessionFactory());
    }

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    // Método auxiliar para comprobar si la base de datos responde.
    private boolean hayConexion() {
        try (Session session = DataProvider.getSessionFactory().openSession()) {
            // Intentamos ejecutar una consulta nativa muy ligera (SELECT 1)
            // Esto obliga a Hibernate a tocar la base de datos real.
            session.createNativeQuery("SELECT 1", Integer.class).uniqueResult();
            return true;
        } catch (Exception e) {
            // Si salta cualquier excepción (JDBCConnectionException, etc.), es que no hay red o BD apagada
            return false;
        }
    }

    // Modificación del método para especificar mejor el error de fallo de conexión
    public void inicio(ActionEvent actionEvent) throws IOException {
        if (!hayConexion()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Conexión");
            alert.setHeaderText("No se pudo conectar a la base de datos");
            alert.setContentText("Por favor, verifica que el servidor esté conectado.");
            alert.showAndWait();
            return;
        }

        // Si hay conexión sigue la lógica normal
        List<Usuario> usuarios = usuarioRepository.findAll();
        Usuario usuarioLogueado = null;
        boolean iniciar = false;

        for (Usuario usuario : usuarios) {
            if (usuario.getNombre_usuario().equals(tfUsuario.getText()) && usuario.getContraseña().equals(pfContra.getText())) {
                iniciar = true;
                usuarioLogueado = usuario;
                break;
            }
        }

        if (iniciar) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/principal-view.fxml"));
            Parent root = loader.load();

            PrincipalController principalController = loader.getController();
            principalController.setUsuarioLogueado(usuarioLogueado);
            principalController.setSessionFactory(DataProvider.getSessionFactory());

            Stage stage = new Stage();
            stage.setTitle("Lista de copias");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            Stage loginStage = (Stage) btnInicio.getScene().getWindow();
            loginStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Credenciales Incorrectas");
            alert.setHeaderText("Acceso denegado");
            alert.setContentText("El usuario o la contraseña no coinciden.");
            alert.showAndWait();
        }
    }

}