module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;        // Add this for WebView
    requires java.desktop;      // Add this for Desktop class

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires jbcrypt;

    // Export packages so JavaFX can access them
    requires org.json;
    requires javafx.graphics;

    // Also export your main package
    exports org.example.demo1;
    exports org.example.demo1.controllers to javafx.fxml;
    exports org.example.demo1.controllers.visualizations to javafx.fxml;  // 🔹 Make this visible

    // Opens packages for reflection-based FXML loading
    opens org.example.demo1 to javafx.fxml;
    opens org.example.demo1.controllers.visualizations to javafx.fxml; // 🔹 Important!
    // Opens packages for FXML reflection
    opens org.example.demo1.controllers to javafx.fxml, javafx.base;
    exports org.example.demo1.utils to javafx.fxml;
    opens org.example.demo1.utils to javafx.fxml;
    exports org.example.demo1.controllers.problems to javafx.fxml;
    opens org.example.demo1.controllers.problems to javafx.base, javafx.fxml;
}
