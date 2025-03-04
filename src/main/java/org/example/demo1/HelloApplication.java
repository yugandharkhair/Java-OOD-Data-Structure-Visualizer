package org.example.demo1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo1.utils.MongoDBConnection;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize MongoDB connection
        try {
            MongoDBConnection.getDatabase();
            System.out.println("MongoDB connection initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            e.printStackTrace();

            // You might want to show an error dialog here
            // For now, we'll just continue with the application
        }

        // Use the correct path to match your project structure
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/demo1/dashboard.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1500, 1000);
        stage.setTitle("Data Structures Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // Close MongoDB connection when application exits
        MongoDBConnection.closeConnection();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch();
    }
}