module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires jbcrypt;

    requires java.desktop;
    requires org.json;
    requires javafx.graphics;



    // Export your controller package to javafx.fxml
    exports org.example.demo1.controllers to javafx.fxml;

    // Also export your main package
    exports org.example.demo1;

    // Opens packages for FXML reflection
    opens org.example.demo1.controllers to javafx.fxml, javafx.base;
    opens org.example.demo1 to javafx.fxml;
    exports org.example.demo1.utils to javafx.fxml;
    opens org.example.demo1.utils to javafx.fxml;
    exports org.example.demo1.controllers.problems to javafx.fxml;
    opens org.example.demo1.controllers.problems to javafx.base, javafx.fxml;
}