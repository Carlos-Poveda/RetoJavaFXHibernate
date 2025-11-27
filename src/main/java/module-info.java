module org.example.retofxhibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires javafx.graphics;
    requires jakarta.persistence;
    requires java.naming;
    requires javafx.base;


    opens org.example.retofxhibernate to javafx.fxml;
    opens org.example.retofxhibernate.Controllers to javafx.fxml;

    opens org.example.retofxhibernate.Usuario to org.hibernate.orm.core;
    opens org.example.retofxhibernate.Copia to org.hibernate.orm.core;


    exports org.example.retofxhibernate;
    exports org.example.retofxhibernate.Controllers;
}