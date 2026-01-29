package org.example.retofxhibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.retofxhibernate.Common.DataProvider;
import org.example.retofxhibernate.Usuario.Usuario;
import org.example.retofxhibernate.Usuario.UsuarioRepository;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            System.out.println("Iniciando aplicación...");
            insertarDatosIniciales();
            System.out.println("Datos iniciales verificados.");
            
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
            System.out.println("Ventana mostrada.");
        } catch (Throwable e) {
            System.err.println("ERROR FATAL AL INICIAR LA APLICACIÓN:");
            e.printStackTrace();
        }
    }

    private void insertarDatosIniciales() {
        try {
            EntityManagerFactory emf = DataProvider.getEntityManagerFactory();
            UsuarioRepository usuarioRepository = new UsuarioRepository(emf);

            // Comprobamos si ya existen usuarios para no duplicarlos cada vez que inicies
            if (usuarioRepository.findAll().isEmpty()) {
                System.out.println("Base de datos vacía. Insertando usuarios por defecto...");

                // Crear Administrador (ID 1 se asignará automáticamente o manualmente según tu entidad)
                Usuario admin = new Usuario();
                admin.setNombre_usuario("admin");
                admin.setContraseña("admin1");

                // Crear Usuario normal de prueba
                Usuario user = new Usuario();
                user.setNombre_usuario("user");
                user.setContraseña("1234");

                usuarioRepository.save(admin);
                usuarioRepository.save(user);

                System.out.println("Usuarios insertados: admin/admin y user/1234");
            } else {
                System.out.println("La base de datos ya contiene usuarios.");
            }
        } catch (Exception e) {
            System.err.println("Error en insertarDatosIniciales:");
            e.printStackTrace();
            throw new RuntimeException(e); // Re-lanzar para detener la app si la DB falla
        }
    }

    public static void main(String[] args) {
        launch();
    }
}