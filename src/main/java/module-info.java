module org.example.retofxhibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;


    opens org.example.retofxhibernate to javafx.fxml;
    exports org.example.retofxhibernate;
    exports org.example.retofxhibernate.Controllers;
    opens org.example.retofxhibernate.Controllers to javafx.fxml;
}