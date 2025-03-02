package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class VisualizationController {

    @FXML
    protected Button backButton;

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        navigateToDashboard();
    }


    @FXML
    private void onArrayClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualizations/array_visualization.fxml", event);
    }

    @FXML
    private void onLinkedListClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualizations/array_visualization.fxml", event);
    }

    @FXML
    private void onStackClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualizations/stack_visualization.fxml", event);
    }

    @FXML
    private void onQueueClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualizations/queue_visualization.fxml", event);
    }

    @FXML
    private void onTreeClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualizations/array_visualization.fxml", event);
    }

    @FXML
    private void onGraphClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/visualizations/array_visualization.fxml", event);
    }


    private void navigateToScreen(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); // Dynamically get stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile);
        }
    }

    protected void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading dashboard.fxml");
        }
    }


}
