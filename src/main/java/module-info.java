module org.example.retofxhibernate {
    // Dependencias de JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Dependencia de ObjectDB (nombre automático derivado del jar objectdb-2.8.1.jar)
    // Este módulo incluye también las clases de javax.persistence
    requires objectdb;

    // Módulo necesario para java.sql.Date y otros tipos SQL usados por ObjectDB
    requires java.sql;
    requires javax.persistence;

    // Abrir paquetes para que JavaFX y ObjectDB puedan acceder a ellos
    opens org.example.retofxhibernate to javafx.fxml;
    opens org.example.retofxhibernate.Controllers to javafx.fxml;
    
    // Abrir el paquete de entidades a ObjectDB para que pueda hacer reflexión
    opens org.example.retofxhibernate.Usuario to objectdb;

    // Exportar el paquete principal
    exports org.example.retofxhibernate;
}