package org.example.retofxhibernate.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {


    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContra;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnSalir;

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void inicio(ActionEvent actionEvent) {

    }
}