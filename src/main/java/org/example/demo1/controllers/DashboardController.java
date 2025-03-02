package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.demo1.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

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
    private Label welcomeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check if user is logged in
        if (UserSession.getInstance().isLoggedIn()) {
            // Update login button to show username
            loginButton.setText("Logged In");
            loginButton.setDisable(true);

            // Update welcome message
            welcomeLabel.setText("Welcome back, " + UserSession.getInstance().getCurrentUser().getUsername() + "!");

            // Enable all buttons
            enableAllButtons(true);
        } else {
            // Reset login button
            loginButton.setText("Login");
            loginButton.setDisable(false);

            // Reset welcome message
            welcomeLabel.setText("Welcome to Data Structures Visualizer");

            // Disable all buttons except login
            enableAllButtons(false);
        }
    }

    private void enableAllButtons(boolean enable) {
        // Login button is handled separately

        // Only enable these buttons if the user is logged in
        catalogButton.setDisable(!enable);
        visualizationButton.setDisable(!enable);
        tutorialButton.setDisable(!enable);
        problemsButton.setDisable(!enable);
        profileButton.setDisable(!enable);

        // Add styling to show disabled state more clearly
        String disabledStyle = "-fx-opacity: 0.5;";
        String enabledStyle = "";

        catalogButton.setStyle(enable ? enabledStyle : disabledStyle);
        visualizationButton.setStyle(enable ? enabledStyle : disabledStyle);
        tutorialButton.setStyle(enable ? enabledStyle : disabledStyle);
        problemsButton.setStyle(enable ? enabledStyle : disabledStyle);
        profileButton.setStyle(enable ? enabledStyle : disabledStyle);
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        navigateToScreen("org/example/demo1/login.fxml");
    }

    @FXML
    private void onCatalogButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/catalog.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onVisualizationButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/visualization.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onTutorialButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/tutorial.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onProblemsButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/problems_frontend/problemsdashboard.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onProfileButtonClick(ActionEvent event) {
        // Check if user is logged in before going to profile
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/profile.fxml");
        } else {
            promptLogin();
        }
    }

    private void promptLogin() {
        // Show login dialog
        navigateToScreen("org/example/demo1/login.fxml");
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