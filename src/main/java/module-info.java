module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires jbcrypt;

    // Export packages so JavaFX can access them
    exports org.example.demo1;
    exports org.example.demo1.controllers to javafx.fxml;
    exports org.example.demo1.controllers.visualizations to javafx.fxml;  // 🔹 Make this visible

    // Opens packages for reflection-based FXML loading
    opens org.example.demo1 to javafx.fxml;
    opens org.example.demo1.controllers to javafx.fxml;
    opens org.example.demo1.controllers.visualizations to javafx.fxml; // 🔹 Important!
}
