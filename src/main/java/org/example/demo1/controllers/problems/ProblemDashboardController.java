package org.example.demo1.controllers.problems;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ProblemDashboardController {

    @FXML
    private Button backButton;

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        navigateToScreen("/org/example/demo1/dashboard.fxml"); // Go back to Main Dashboard
    }

    @FXML
    private void onArraysClick(ActionEvent event) {
        ProblemsController.setProblemType("arrays");
        navigateToScreen("/org/example/demo1/problems_frontend/problems.fxml");  // Load common FXML file
    }

    @FXML
    private void onLinkedListsClick(ActionEvent event) {
        ProblemsController.setProblemType("linked_lists");
        navigateToScreen("/org/example/demo1/problems_frontend/problems.fxml");
    }

    @FXML
    private void onStacksClick(ActionEvent event) {
        ProblemsController.setProblemType("stacks");
        navigateToScreen("/org/example/demo1/problems_frontend/problems.fxml");
    }

    @FXML
    private void onQueuesClick(ActionEvent event) {
        ProblemsController.setProblemType("queues");
        navigateToScreen("/org/example/demo1/problems_frontend/problems.fxml");
    }

    @FXML
    private void onTreesClick(ActionEvent event) {
        ProblemsController.setProblemType("trees");
        navigateToScreen("/org/example/demo1/problems_frontend/problems.fxml");
    }

    @FXML
    private void onGraphsClick(ActionEvent event) {
        ProblemsController.setProblemType("graphs");
        navigateToScreen("/org/example/demo1/problems_frontend/problems.fxml");
    }

    private void navigateToScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile);
        }
    }
}
