package org.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Use the correct path to match your project structure
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/demo1/dashboard.fxml"));

        // Alternative approach if the above doesn't work
//         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("org/example/demo1/dashboard.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Data Structures Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}