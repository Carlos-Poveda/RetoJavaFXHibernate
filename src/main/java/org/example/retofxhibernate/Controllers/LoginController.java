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
        usuarioRepository = new UsuarioRepository(DataProvider.getSessionFactory());
    }

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void inicio(ActionEvent actionEvent) throws IOException {
        List<Usuario> usuarios = usuarioRepository.findAll();
        Usuario usuarioLogueado = null;
        boolean iniciar = false;
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre_usuario().equals(tfUsuario.getText()) && usuario.getContraseña().equals(pfContra.getText())) {
                iniciar = true;
                usuarioLogueado = usuario;

            }
        }
        if (iniciar) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/retofxhibernate/principal-view.fxml"));
            Parent root = loader.load();

            PrincipalController principalController = loader.getController();
            principalController.setUsuarioLogueado(usuarioLogueado);
            principalController.setCopiaRepository(DataProvider.getSessionFactory());

            Stage stage = new Stage();
            stage.setTitle("Lista de copias");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            Stage loginStage = (Stage) btnInicio.getScene().getWindow();
            loginStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Las credenciales no son válidas");
            alert.setContentText("El usuario o la contraseña no son correctos.");
            alert.showAndWait();
        }
    }

}