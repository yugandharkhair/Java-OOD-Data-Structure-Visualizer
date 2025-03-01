package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button loginButton;

    @FXML
    private Button catalogButton;

    @FXML
    private Button visualizationButton;

    @FXML
    private Button tutorialButton;

    @FXML
    private Button problemsButton;

    @FXML
    private Button profileButton;

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/login.fxml");
    }

    @FXML
    private void onCatalogButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/catalog.fxml");
    }

    @FXML
    private void onVisualizationButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualization.fxml");
    }

    @FXML
    private void onTutorialButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/tutorial.fxml");
    }

    @FXML
    private void onProblemsButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/problems.fxml");
    }

    @FXML
    private void onProfileButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/profile.fxml");
    }

    private void navigateToScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile);
        }
    }
}