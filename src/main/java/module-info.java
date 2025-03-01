module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    // Export your controller package to javafx.fxml
    exports org.example.demo1.controllers to javafx.fxml;

    // Also export your main package
    exports org.example.demo1;

    // Opens packages for FXML reflection
    opens org.example.demo1.controllers to javafx.fxml;
    opens org.example.demo1 to javafx.fxml;
}